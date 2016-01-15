package org.thanatos.flowgeek.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.thanatos.flowgeek.AppManager;
import org.thanatos.flowgeek.R;

import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by thanatos on 15-7-21.
 */
public abstract class BaseTabFragment extends BaseFragment{

    @Bind(R.id.view_pager) ViewPager viewPager;

    protected FragmentStatePagerAdapter mAdapter;
    protected ArrayList<ViewPageInfo> mTabs;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (mAdapter==null){
            mTabs = new ArrayList<>();
            onSetupTabs();

            viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return mTabs.get(position).fragment;
                }
                @Override
                public int getCount() {
                    return mTabs.size();
                }
                @Override
                public CharSequence getPageTitle(int position) {
                    return mTabs.get(position).tag;
                }
            });

        }else{
            viewPager.setAdapter(mAdapter);
        }
    }

    /**
     * 导航元素
     * @param title
     * @return
     */
    public abstract View setupTabItemView(String title);

    /**
     * 设置Fragment
     */
    public abstract void onSetupTabs();

    /**
     * 添加Fragment对象到ViewPager
     */
    public void addTab(String tag, Class<? extends Fragment> fragment, int catalog){
        Bundle bundle = new Bundle();
        bundle.putInt(AppManager.BUNDLE_TYPE, catalog);
        mTabs.add(new ViewPageInfo(tag, Fragment.instantiate(getActivity(), fragment.getName(), bundle)));
    }

    public void addTab(String tag, Class<? extends Fragment> fragment){
        mTabs.add(new ViewPageInfo(tag, Fragment.instantiate(getActivity(), fragment.getName())));
    }

    public void addTab(String tag, Fragment fragment){
        mTabs.add(new ViewPageInfo(tag, fragment));
    }

    public void setCurrentItem(int index){
        viewPager.setCurrentItem(index);
    }

    public int getCurrentItem(){
        return viewPager.getCurrentItem();
    }

    public int getPageCount(){
        return mTabs.size();
    }

    /**
     * ViewPageInformation
     */
    public static class ViewPageInfo {
        public String tag;
        public View view;
        public Fragment fragment;

        public ViewPageInfo(String tag, Fragment fragment){
            this.tag = tag;
            this.fragment = fragment;
        }
    }
}
