package com.dy.fastframework.fragment;


import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dy.fastframework.tablayout.RecyclerTabLayout;

import java.util.List;
/**
 * 用RecyclerTabLayout封装的标签布局的基类，可直接继承使用
 */
public abstract class BaseTabViewFragment extends BaseFragment {
    public RecyclerTabLayout recyclerTabLayout;
    public ViewPager viewpager;
    public List<Fragment> fgs;
    public List<String> titles;
    public BasePagerAdapter fragmentAdapter;

    public abstract RecyclerTabLayout setTabLayout();
    public abstract ViewPager setViewPager();
    public abstract List<Fragment> setFragments();
    public abstract List<String> setTitles();
    public abstract RecyclerTabLayout.OnPageSelectedListener setOnPageSelectedListener();
    /**
     * 直接在需要初始化的地方调用此方法即可
     */
    public void initPageItem(){
        recyclerTabLayout=setTabLayout();
        viewpager=setViewPager();
        fgs=setFragments();
        titles=setTitles();
        fragmentAdapter=new BasePagerAdapter(getChildFragmentManager(), fgs,titles);
        viewpager.setOffscreenPageLimit(titles.size());
        viewpager.setAdapter(fragmentAdapter);
        recyclerTabLayout.setUpWithViewPager(viewpager);
        recyclerTabLayout.setOnPageSelectedListener(setOnPageSelectedListener());
    }


}
