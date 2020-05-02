package com.account.work.fragment.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Lazy loading Fragment
 */
public abstract class BaseLazyFragment extends Fragment {

    protected View mRootView;
    protected Context mContext;
    protected boolean isVisible;
    private boolean isPrepared;
    private boolean isFirst = true;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        initPrepare();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            setUserVisibleHint(true);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = initView(inflater, container, savedInstanceState);
        }

        return mRootView;
    }

    /**
     * Lazy load
     */
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirst) {
            return;
        }
        initData();
        isFirst = false;
    }


    /**
     * The method invoked in onActivityCreated can be used to initialize operations.
     */
    protected void initPrepare(){};

    /**
     * Fragment is called when it is not visible
     */
    protected  void onInvisible(){}

    /**
     * Here, get the data, refresh the interface
     */
    protected abstract void initData();

    /**
     * Initialize the layout. Please don't put the time-consuming
     * operation in this method. This method is used to provide one
     *
     * The basic layout rather than a complete layout, so as to
     * avoid ViewPager pre loading consumes a lot of resources.
     */
    protected abstract View initView(LayoutInflater inflater,
                                     @Nullable ViewGroup container,
                                     @Nullable Bundle savedInstanceState);
}