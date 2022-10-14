package com.sjp.framework.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;

import com.sjp.framework.channel.ChannelStore;


public abstract class Fragment<P extends UseViewModel> extends androidx.fragment.app.Fragment implements FragmentSupport<P> {

    //======================================================================
    // Variables
    //======================================================================

    private final FragmentDelegate<P> mDelegate = new FragmentDelegate<P>() {
        @Override
        protected androidx.fragment.app.Fragment getFragment() {
            return Fragment.this;
        }
    };

    //======================================================================
    // Override Methods
    //======================================================================

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState, this::hashCode);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDelegate.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mDelegate.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDelegate.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mDelegate.onResume();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mDelegate.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mDelegate.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //Modify 2016. 4. 18. lsh Fragment.onCreate() 보다 먼저 호출되는 경우 있음
        mDelegate.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDelegate.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDelegate.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDelegate.onDestroy();
    }

    @Deprecated
    @Override
    public final P getPresenter() {
        return mDelegate.getViewModel();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final P getViewmodel() {
        return mDelegate.getViewModel();
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    public final boolean isFragmentActivated() {
        return mDelegate.isFragmentActivated();
    }

    /*@Override
    public AppCompatActivityDelegate getActivityDelegate() {
        return mDelegate.getActivityDelegate();
    }*/

    @Override
    public void popBackStackImmediate() {
        mDelegate.popBackStackImmediate();
    }

    @Override
    public ChannelStore getChannelStore() {
        return mDelegate.getStore();
    }

    //======================================================================
    // Public Methods
    //======================================================================

    public static androidx.fragment.app.Fragment findFragment(int key) {
        return FragmentDelegate.findFragment(key);
    }

    public static UseViewModel findViewModel(androidx.fragment.app.Fragment fragment, Class<? extends UseViewModel> modelClass) {
        return FragmentDelegate.findViewModel(fragment, modelClass);
    }
}
