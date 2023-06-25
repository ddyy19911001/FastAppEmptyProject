package com.vise.xsnow.http.config;

import com.vise.utils.assist.SSLUtil;
import com.vise.xsnow.common.ViseConfig;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.core.ApiCookie;
import com.vise.xsnow.http.exception.ApiException;
import com.vise.xsnow.http.exception.IBaseRequestErroLitener;
import com.vise.xsnow.http.interceptor.GzipRequestInterceptor;
import com.vise.xsnow.http.interceptor.OfflineCacheInterceptor;
import com.vise.xsnow.http.interceptor.OnlineCacheInterceptor;
import com.vise.xsnow.http.mode.ApiHost;
import com.vise.xsnow.util.RequestTimer;

import java.io.File;
import java.net.Proxy;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Call.Factory;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;

/**
 * @Description: 请求全局配置
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 2017-04-28 17:17
 */
public class HttpGlobalConfig {
    private CallAdapter.Factory callAdapterFactory;//Call适配器工厂
    private Converter.Factory converterFactory;//转换工厂
    private Factory callFactory;//Call工厂
    private SSLSocketFactory sslSocketFactory;//SSL工厂
    private HostnameVerifier hostnameVerifier;//主机域名验证
    private ConnectionPool connectionPool;//连接池
    private Map<String, String> globalParams = new LinkedHashMap<>();//请求参数
    private Map<String, String> globalHeaders = new LinkedHashMap<>();//请求头
    private boolean isHttpCache;//是否使用Http缓存
    private File httpCacheDirectory;//Http缓存路径
    private Cache httpCache;//Http缓存对象
    private boolean isCookie;//是否使用Cookie
    private ApiCookie apiCookie;//Cookie配置
    private String baseUrl;//基础域名
    private int retryDelayMillis;//请求失败重试间隔时间
    private int retryCount;//请求失败重试次数
    private boolean needLog=true;
    public String logTag="RxHttpLog";
    private static HttpGlobalConfig instance;
    private IBaseRequestErroLitener httpErroListener;//网络请求错误时回调
    private static OnResponseInfoGetLitener onResponseInfoGetLitener;//获取到服务器返回数据并解析成对象后回调
    private int totalTime=2;//设置超过多少秒开始显示转圈
    public OnRequestWatingDialogListener onRequestWatingDialogListener;//设置监听
    private boolean needShowLoading=false;//设置超过一定时间是否显示转圈
    public HashMap<String,RequestTimer> timers=new HashMap<>();
    private HttpGlobalConfig() {
    }

    public static HttpGlobalConfig getInstance() {
        if (instance == null) {
            synchronized (HttpGlobalConfig.class) {
                if (instance == null) {
                    instance = new HttpGlobalConfig();
                    initDefaultSetting();
                }
            }
        }
        return instance;
    }

    public void setTotalTime(int timesecond){
        totalTime=timesecond;
    }

    public interface OnRequestWatingDialogListener{
        //最大等待时间结束，需要显示转圈圈
        void onTimeOverToShowLoading();
        //请求成功了，不需要显示转圈圈
        void onRequestOverLoadingNeedClose();
    }

    public void setOnRequestWatingDialogListener(OnRequestWatingDialogListener onRequestWatingDialogListener) {
        this.onRequestWatingDialogListener = onRequestWatingDialogListener;
    }



    public void onErroRemoveAllTimer(){
        timers.clear();
        if(onRequestWatingDialogListener!=null) {
            onRequestWatingDialogListener.onRequestOverLoadingNeedClose();
        }
    }


    public void startTimer(final String requestUrl){
        if(needShowLoading) {
            final RequestTimer timer = new RequestTimer();
            timer.setIntervalTime(1000);
            timer.setTotalTime(1000 * totalTime);
            timer.setTimerLiener(new RequestTimer.TimeListener() {
                @Override
                public void onFinish() {
                    if(timers.isEmpty()){
                        return;
                    }
                    RequestTimer lastRequestTimer=timers.get(requestUrl);
                    if (onRequestWatingDialogListener!= null&&lastRequestTimer!=null) {
                        onRequestWatingDialogListener.onTimeOverToShowLoading();
                        timers.remove(requestUrl);
                    }
                }

                @Override
                public void onInterval(long remainTime) {

                }
            });
            timer.start();
            timers.put(requestUrl, timer);
        }
    }

    public void setNeedShowLoading(boolean needShowLoading) {
        this.needShowLoading = needShowLoading;
    }

    public  <T>void onInfoGet(final T data){
        if(onResponseInfoGetLitener!=null){
            Observable observable= Observable.create(new ObservableOnSubscribe<T>() {
                @Override
                public void subscribe(ObservableEmitter<T> e) throws Exception {
                    e.onNext(data);
                }
            });
            observable.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<T>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(T value) {
                    onResponseInfoGetLitener.onInfoGet(value);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }
    }



    public interface OnResponseInfoGetLitener{
        <T>void onInfoGet(T data);
    }

    /**
     * 设置默认配置
     */
    public static void initDefaultSetting() {
        instance.SSLSocketFactory(SSLUtil.getSslSocketFactory(null,null,null));
    }


    /**
     * 设置CallAdapter工厂
     *
     * @param factory
     * @return
     */
    public HttpGlobalConfig callAdapterFactory(CallAdapter.Factory factory) {
        this.callAdapterFactory = factory;
        return this;
    }

    /**
     * 设置转换工厂
     *
     * @param factory
     * @return
     */
    public HttpGlobalConfig converterFactory(Converter.Factory factory) {
        this.converterFactory = factory;
        return this;
    }

    /**
     * 设置Call的工厂
     *
     * @param factory
     * @return
     */
    public HttpGlobalConfig callFactory(Factory factory) {
        this.callFactory = checkNotNull(factory, "factory == null");
        return this;
    }

    public HttpGlobalConfig needLog(boolean needLog){
        this.needLog=needLog;
        return this;
    }

    public HttpGlobalConfig setTag(String tag){
        this.logTag=tag;
        return this;
    }

    public HttpGlobalConfig httpErroListener(IBaseRequestErroLitener erroListener){
        this.httpErroListener=erroListener;
        return this;
    }

    public HttpGlobalConfig responseInfoListener(OnResponseInfoGetLitener responseInfoGetLitener){
        this.onResponseInfoGetLitener=responseInfoGetLitener;
        return this;
    }

    public  String getTag() {
        String tag = "(%s.%sLine:%d)"; // 占位符
        StackTraceElement caller = Thread.currentThread().getStackTrace()[5];
        String callerClazzName = caller.getClassName(); // 获取到类名
        callerClazzName = callerClazzName.substring(callerClazzName
                .lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(),
                caller.getLineNumber()); // 替换
        tag = logTag + "-" + tag;
        return tag;
    }


    /**
     * 设置SSL工厂
     *
     * @param sslSocketFactory
     * @return
     */
    public HttpGlobalConfig SSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
        return this;
    }

    /**
     * 设置主机验证机制
     *
     * @param hostnameVerifier
     * @return
     */
    public HttpGlobalConfig hostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    /**
     * 设置连接池
     *
     * @param connectionPool
     * @return
     */
    public HttpGlobalConfig connectionPool(ConnectionPool connectionPool) {
        this.connectionPool = checkNotNull(connectionPool, "connectionPool == null");
        return this;
    }

    /**
     * 设置请求头部
     *
     * @param globalHeaders
     * @return
     */
    public HttpGlobalConfig globalHeaders(Map<String, String> globalHeaders) {
        if (globalHeaders != null) {
            this.globalHeaders = globalHeaders;
        }
        return this;
    }

    /**
     * 设置请求参数
     *
     * @param globalParams
     * @return
     */
    public HttpGlobalConfig globalParams(Map<String, String> globalParams) {
        if (globalParams != null) {
            this.globalParams = globalParams;
        }
        return this;
    }

    /**
     * 设置是否添加HTTP缓存
     *
     * @param isHttpCache
     * @return
     */
    public HttpGlobalConfig setHttpCache(boolean isHttpCache) {
        this.isHttpCache = isHttpCache;
        return this;
    }

    /**
     * 设置HTTP缓存路径
     *
     * @param httpCacheDirectory
     * @return
     */
    public HttpGlobalConfig setHttpCacheDirectory(File httpCacheDirectory) {
        this.httpCacheDirectory = httpCacheDirectory;
        return this;
    }

    /**
     * 设置HTTP缓存
     *
     * @param httpCache
     * @return
     */
    public HttpGlobalConfig httpCache(Cache httpCache) {
        this.httpCache = httpCache;
        return this;
    }

    /**
     * 设置是否添加Cookie
     *
     * @param isCookie
     * @return
     */
    public HttpGlobalConfig setCookie(boolean isCookie) {
        this.isCookie = isCookie;
        return this;
    }

    /**
     * 设置Cookie管理
     *
     * @param cookie
     * @return
     */
    public HttpGlobalConfig apiCookie(ApiCookie cookie) {
        this.apiCookie = checkNotNull(cookie, "cookieManager == null");
        return this;
    }

    /**
     * 设置请求baseUrl
     *
     * @param baseUrl
     * @return
     */
    public HttpGlobalConfig baseUrl(String baseUrl) {
        this.baseUrl = checkNotNull(baseUrl, "baseUrl == null");
        ApiHost.setHost(this.baseUrl);
        return this;
    }

    /**
     * 设置请求失败重试间隔时间
     *
     * @param retryDelayMillis
     * @return
     */
    public HttpGlobalConfig retryDelayMillis(int retryDelayMillis) {
        this.retryDelayMillis = retryDelayMillis;
        return this;
    }

    /**
     * 设置请求失败重试次数
     *
     * @param retryCount
     * @return
     */
    public HttpGlobalConfig retryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    /**
     * 设置代理
     *
     * @param proxy
     * @return
     */
    public HttpGlobalConfig proxy(Proxy proxy) {
        ViseHttp.getOkHttpBuilder().proxy(checkNotNull(proxy, "proxy == null"));
        return this;
    }

    /**
     * 设置连接超时时间（秒）
     *
     * @param timeout
     * @return
     */
    public HttpGlobalConfig connectTimeout(int timeout) {
        return connectTimeout(timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置读取超时时间（秒）
     *
     * @param timeout
     * @return
     */
    public HttpGlobalConfig readTimeout(int timeout) {
        return readTimeout(timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置写入超时时间（秒）
     *
     * @param timeout
     * @return
     */
    public HttpGlobalConfig writeTimeout(int timeout) {
        return writeTimeout(timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置连接超时时间
     *
     * @param timeout
     * @param unit
     * @return
     */
    public HttpGlobalConfig connectTimeout(int timeout, TimeUnit unit) {
        if (timeout > -1) {
            ViseHttp.getOkHttpBuilder().connectTimeout(timeout, unit);
        } else {
            ViseHttp.getOkHttpBuilder().connectTimeout(ViseConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        }
        return this;
    }

    /**
     * 设置写入超时时间
     *
     * @param timeout
     * @param unit
     * @return
     */
    public HttpGlobalConfig writeTimeout(int timeout, TimeUnit unit) {
        if (timeout > -1) {
            ViseHttp.getOkHttpBuilder().writeTimeout(timeout, unit);
        } else {
            ViseHttp.getOkHttpBuilder().writeTimeout(ViseConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        }
        return this;
    }

    /**
     * 设置读取超时时间
     *
     * @param timeout
     * @param unit
     * @return
     */
    public HttpGlobalConfig readTimeout(int timeout, TimeUnit unit) {
        if (timeout > -1) {
            ViseHttp.getOkHttpBuilder().readTimeout(timeout, unit);
        } else {
            ViseHttp.getOkHttpBuilder().readTimeout(ViseConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        }
        return this;
    }

    /**
     * 设置拦截器
     *
     * @param interceptor
     * @return
     */
    public HttpGlobalConfig interceptor(Interceptor interceptor) {
        ViseHttp.getOkHttpBuilder().addInterceptor(checkNotNull(interceptor, "interceptor == null"));
        return this;
    }

    /**
     * 设置网络拦截器
     *
     * @param interceptor
     * @return
     */
    public HttpGlobalConfig networkInterceptor(Interceptor interceptor) {
        ViseHttp.getOkHttpBuilder().addNetworkInterceptor(checkNotNull(interceptor, "interceptor == null"));
        return this;
    }

    /**
     * 使用POST方式是否需要进行GZIP压缩，服务器不支持则不设置
     *
     * @return
     */
    public HttpGlobalConfig postGzipInterceptor() {
        interceptor(new GzipRequestInterceptor());
        return this;
    }

    /**
     * 设置在线缓存，主要针对网路请求过程进行缓存
     *
     * @param httpCache
     * @return
     */
    public HttpGlobalConfig cacheOnline(Cache httpCache) {
        networkInterceptor(new OnlineCacheInterceptor());
        this.httpCache = httpCache;
        return this;
    }

    /**
     * 设置在线缓存，主要针对网路请求过程进行缓存
     *
     * @param httpCache
     * @param cacheControlValue
     * @return
     */
    public HttpGlobalConfig cacheOnline(Cache httpCache, final int cacheControlValue) {
        networkInterceptor(new OnlineCacheInterceptor(cacheControlValue));
        this.httpCache = httpCache;
        return this;
    }

    /**
     * 设置离线缓存，主要针对网路请求过程进行缓存
     *
     * @param httpCache
     * @return
     */
    public HttpGlobalConfig cacheOffline(Cache httpCache) {
        networkInterceptor(new OfflineCacheInterceptor(ViseHttp.getContext()));
        interceptor(new OfflineCacheInterceptor(ViseHttp.getContext()));
        this.httpCache = httpCache;
        return this;
    }

    /**
     * 设置离线缓存，主要针对网路请求过程进行缓存
     *
     * @param httpCache
     * @param cacheControlValue
     * @return
     */
    public HttpGlobalConfig cacheOffline(Cache httpCache, final int cacheControlValue) {
        networkInterceptor(new OfflineCacheInterceptor(ViseHttp.getContext(), cacheControlValue));
        interceptor(new OfflineCacheInterceptor(ViseHttp.getContext(), cacheControlValue));
        this.httpCache = httpCache;
        return this;
    }

    public CallAdapter.Factory getCallAdapterFactory() {
        return callAdapterFactory;
    }

    public Converter.Factory getConverterFactory() {
        return converterFactory;
    }

    public Factory getCallFactory() {
        return callFactory;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public Map<String, String> getGlobalParams() {
        return globalParams;
    }

    public Map<String, String> getGlobalHeaders() {
        return globalHeaders;
    }

    public boolean isHttpCache() {
        return isHttpCache;
    }

    public boolean isCookie() {
        return isCookie;
    }

    public ApiCookie getApiCookie() {
        return apiCookie;
    }

    public Cache getHttpCache() {
        return httpCache;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public int getRetryDelayMillis() {
        if (retryDelayMillis < 0) {
            retryDelayMillis = ViseConfig.DEFAULT_RETRY_DELAY_MILLIS;
        }
        return retryDelayMillis;
    }

    public int getRetryCount() {
        if (retryCount < 0) {
            retryCount = ViseConfig.DEFAULT_RETRY_COUNT;
        }
        return retryCount;
    }

    public File getHttpCacheDirectory() {
        return httpCacheDirectory;
    }

    private <T> T checkNotNull(T t, String message) {
        if (t == null) {
            throw new NullPointerException(message);
        }
        return t;
    }

    public void onHttpErro(ApiException e) {
        if(httpErroListener!=null){
            httpErroListener.onHttpErro(e);
        }
    }
}
