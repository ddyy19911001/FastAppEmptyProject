package com.vise.xsnow.http.request;

import android.text.TextUtils;
import android.util.Log;

import com.vise.xsnow.common.GsonUtil;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.core.ApiManager;
import com.vise.xsnow.http.mode.CacheResult;
import com.vise.xsnow.http.mode.MediaTypes;
import com.vise.xsnow.http.subscriber.ApiCallbackSubscriber;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @Description: Post请求
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 2017-04-28 16:06
 */
public class PostRequest extends BaseHttpRequest<PostRequest> {

    protected Map<String, Object> forms = new LinkedHashMap<>();
    protected StringBuilder stringBuilder = new StringBuilder();
    protected RequestBody requestBody;
    protected MediaType mediaType;
    protected String content;

    public PostRequest(String suffixUrl) {
        super(suffixUrl);
    }

    @Override
    protected <T> Observable<T> execute(Type type) {
        if (stringBuilder.length() > 0) {
            suffixUrl = suffixUrl + stringBuilder.toString();
        }
        if (forms != null && forms.size() > 0) {
            if (params != null && params.size() > 0) {
                Iterator<Map.Entry<String, Object>> entryIterator = params.entrySet().iterator();
                Map.Entry<String, Object> entry;
                while (entryIterator.hasNext()) {
                    entry = entryIterator.next();
                    if (entry != null) {
                        forms.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            return apiService.postForm(suffixUrl, forms).compose(this.<T>norTransformer(type,getNowReqeustUrl()));
        }
        if (requestBody != null) {
            return apiService.postBody(suffixUrl, requestBody).compose(this.<T>norTransformer(type,getNowReqeustUrl()));
        }
        if (content != null && mediaType != null) {
            requestBody = RequestBody.create(mediaType, content);
            return apiService.postBody(suffixUrl, requestBody).compose(this.<T>norTransformer(type,getNowReqeustUrl()));
        }
        return apiService.post(suffixUrl, params).compose(this.<T>norTransformer(type,getNowReqeustUrl()));
    }


    @Override
    public void printRequestLog() {
        String paramsStr=this.params.size()==0?"null": GsonUtil.gson().toJson(this.params);
        if(this.params.size()==0&& !TextUtils.isEmpty(this.content)){
            paramsStr=this.content;
        }
        String headerStr=((this.headers.headersMap==null||this.headers.headersMap.size()==0)?"null": this.headers.toJSONString());
        httpGlobalConfig.startTimer((this.baseUrl==null?httpGlobalConfig.getBaseUrl():this.baseUrl)+suffixUrl+"/params="+paramsStr);
        String logTag = httpGlobalConfig.getTag();
        String url = (this.baseUrl == null ? httpGlobalConfig.getBaseUrl() : this.baseUrl) + suffixUrl;
        String requestBodyLog="（请求发送："+url+"）\n请求地址："+url+"\n"+"请求时间："+format.format(new Date())+"\n请求头："+headerStr+"\n请求参数："+paramsStr;
        Log.i(logTag, requestBodyLog);
    }


    @Override
    protected <T> Observable<CacheResult<T>> cacheExecute(Type type) {
        return this.<T>execute(type).compose(ViseHttp.getApiCache().<T>transformer(cacheMode, type));
    }

    @Override
    protected <T> void execute(ACallback<T> callback) {
        DisposableObserver disposableObserver = new ApiCallbackSubscriber(callback);
        if (super.tag != null) {
            ApiManager.get().add(super.tag, disposableObserver);
        }
        if (isLocalCache) {
            this.cacheExecute(getSubType(callback)).subscribe(disposableObserver);
        } else {
            this.execute(getType(callback)).subscribe(disposableObserver);
        }
    }

    public PostRequest addUrlParam(String paramKey, String paramValue) {
        if (paramKey != null && paramValue != null) {
            if (stringBuilder.length() == 0) {
                stringBuilder.append("?");
            } else {
                stringBuilder.append("&");
            }
            stringBuilder.append(paramKey).append("=").append(paramValue);
        }
        return this;
    }

    public PostRequest addForm(String formKey, Object formValue) {
        if (formKey != null && formValue != null) {
            forms.put(formKey, formValue);
        }
        return this;
    }

    public PostRequest setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public PostRequest setString(String string) {
        this.content = string;
        this.mediaType = MediaTypes.TEXT_PLAIN_TYPE;
        return this;
    }

    public PostRequest setString(String string, MediaType mediaType) {
        this.content = string;
        this.mediaType = mediaType;
        return this;
    }

    public PostRequest setJson(String json) {
        this.content = json;
        this.mediaType = MediaTypes.APPLICATION_JSON_TYPE;
        return this;
    }


    public PostRequest setFormFile(String json) {
        this.content = json;
        this.mediaType = MediaTypes.MULTIPART_FORM_DATA_TYPE;
        return this;
    }

    public PostRequest setJson(JSONObject jsonObject) {
        this.content = jsonObject.toString();
        this.mediaType = MediaTypes.APPLICATION_JSON_TYPE;
        return this;
    }

    public PostRequest setJson(JSONArray jsonArray) {
        this.content = jsonArray.toString();
        this.mediaType = MediaTypes.APPLICATION_JSON_TYPE;
        return this;
    }
}
