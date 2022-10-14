package com.sjp.framework.fragment;


import static com.sjp.framework.fragment.Fragment.findFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sjp.framework.channel.ChannelProvider;
import com.sjp.framework.channel.ChannelProviders;


public class UseViewModel extends ViewModel implements FragmentCycle {

    //======================================================================
    // Constants
    //======================================================================

    private final static Bundle EMPTY = new Bundle();

    private final static String KEY_CACHE_INSTANCE_DATA = "KEY_CACHE_INSTANCE_DATA";

    private MutableLiveData<Boolean> bindingNotify = new MutableLiveData<>();

    private OnFragmentProvider fragmentProvider;

    //======================================================================
    // Variables
    //======================================================================

    @IgnoreSaveInstance
    private final int mFragmentKey;

    //======================================================================
    // Constructor
    //======================================================================

    public UseViewModel(@NonNull Fragment fragment) {
        mFragmentKey = fragment.hashCode();
    }

    //======================================================================
    // Override Methods
    //======================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /*mSaveInstanceHelper.onRestoredInstance(getRestoreInstance(savedInstanceState));*/
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // Nothing
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Nothing
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Nothing
    }

    @Override
    protected final void onCleared() {
        super.onCleared();
    }

    //======================================================================
    // Public Methods
    //======================================================================

    public void setFragmentProvider(OnFragmentProvider fragmentProvider) {
        this.fragmentProvider = fragmentProvider;
    }

    @Nullable
    public ChannelProvider getChannelProvider() {
        return ChannelProviders.INSTANCE.of(getFragment());
    }

    public MutableLiveData<Boolean> getBindingNotify() {
        return bindingNotify;
    }

    public void executeViewBinding() {
        bindingNotify.postValue(true);
    }

    public String getString(int id) {
        return requireContext().getString(id);
    }

    //======================================================================
    // Protected Methods
    //======================================================================

    @Nullable
    public Fragment getFragment() {
        int key = fragmentProvider != null ? fragmentProvider.getFragmentHashCode() : -1;
        if (fragmentProvider != null) {
            return findFragment(fragmentProvider.getFragmentHashCode());
        }
        return findFragment(mFragmentKey);
    }

    @Deprecated
    protected Context requireContext() {
        return getFragment().requireContext();
    }

    @Deprecated
    protected Activity requireActivity() {
        return getFragment().requireActivity();
    }

    protected void activityFinish() {
        try {
            getActivity().finish();
        } catch (Exception e) {

        }
    }

    @Nullable
    protected final Activity getActivity() {
        Fragment fragment = getFragment();
        if (fragment != null) {
            return fragment.getActivity();
        }
        return null;
    }

    @Nullable
    protected final Context getContext() {
        Fragment fragment = getFragment();
        if (fragment != null) {
            return fragment.getContext();
        }
        return null;
    }

    protected final Bundle getArguments() {
        Fragment fragment = getFragment();
        if (fragment != null) {
            Bundle bundle = fragment.getArguments();
            if (bundle != null) {
                return bundle;
            }
        }
        return EMPTY;
    }

    protected final Resources getResources() {
        Fragment fragment = getFragment();
        if (fragment != null) {
            return fragment.getResources();
        }
        return null;
    }

    protected final int getTag() {
        Fragment fragment = getFragment();
        if (fragment != null) {
            return fragment.hashCode();
        }
        return -1;
    }

    //======================================================================
    // Private Methods
    //======================================================================

    public interface OnFragmentProvider {
        public int getFragmentHashCode();
    }
}
