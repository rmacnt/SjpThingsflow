package com.sjp.framework.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.sjp.framework.channel.ChannelStore;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

abstract class FragmentDelegate<P extends UseViewModel> implements FragmentCycle {

    //======================================================================
    // Constants
    //======================================================================

    private final static String TAG = "FRAGMENT";

    //======================================================================
    // Variables
    //======================================================================

    private P mViewModel;

    private final static FragmentList sFragmentList = new FragmentList();

    private FragmentSupport mFragmentSupport;

    private ChannelStore mStore = new ChannelStore();

    //======================================================================
    // Abstract Methods
    //======================================================================

    protected abstract Fragment getFragment();

    //======================================================================
    // Override Methods
    //======================================================================

    public void onCreate(Bundle savedInstanceState, UseViewModel.OnFragmentProvider provider) {
        Fragment fragment = getFragment();
        mFragmentSupport = (FragmentSupport) fragment;
        sFragmentList.onCreate(fragment);
        bindViewModel();

        cycle("onCreate -> savedInstanceState : " + savedInstanceState + " hashCode : " + fragment.hashCode() + " viewmodel : " + mViewModel.hashCode());
        if (mViewModel != null) {
            mViewModel.setFragmentProvider(provider);
            mViewModel.onCreate(savedInstanceState);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Fragment fragment = getFragment();
        mFragmentSupport = (FragmentSupport) fragment;
        sFragmentList.onCreate(fragment);
        bindViewModel();

        cycle("onCreate -> savedInstanceState : " + savedInstanceState + " hashCode : " + fragment.hashCode() + " viewmodel : " + mViewModel.hashCode());
        if (mViewModel != null) {
            mViewModel.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        cycle("onResume");
        if (mViewModel != null) {
            mViewModel.onResume();
        }
    }

    @Override
    public void onStart() {
        cycle("onStart");
        if (mViewModel != null) {
            mViewModel.onStart();
        }
    }

    @Override
    public void onPause() {
        cycle("onPause");
        if (mViewModel != null) {
            mViewModel.onPause();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Fragment f = getFragment();
        if (f != null) {
            ViewBindingMapper.INSTANCE.bind(f, getViewModel());
        }
        cycle("onViewCreated -> savedInstanceState : " + savedInstanceState);
        if (mViewModel != null) {
            mViewModel.onViewCreated(view, savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        cycle("onSaveInstanceState -> outState : " + outState);
        if (mViewModel != null) {
            mViewModel.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        cycle("onViewStateRestored -> savedInstanceState : " + savedInstanceState);
        if (mViewModel != null) {
            mViewModel.onViewStateRestored(savedInstanceState);
        }
    }

    @Override
    public void setUserVisibleHint(boolean setUserVisibleHint) {
        cycle("setUserVisibleHint -> setUserVisibleHint : " + setUserVisibleHint);
        if (mViewModel != null) {
            mViewModel.setUserVisibleHint(setUserVisibleHint);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mViewModel != null) {
            mViewModel.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mViewModel != null) {
            mViewModel.onPrepareOptionsMenu(menu);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        cycle("onActivityResult -> requestCode : " + requestCode + " resultCode : " + resultCode + " data : " + data);
        if (mViewModel != null) {
            mViewModel.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroyView() {
        cycle("onDestroyView");
        Fragment f = getFragment();
        if (f != null) {
            ViewBindingMapper.INSTANCE.unbind(f);
        }
    }

    @SuppressWarnings("EmptyCatchBlock")
    @Override
    public void onDestroy() {
        cycle("onDestroy");
        Fragment fragment = getFragment();
        sFragmentList.onDestroy(fragment);

        if (mViewModel != null) {
            mViewModel.onDestroy();
            mViewModel = null;
        }
        mStore.clear();

    }

    //======================================================================
    // Public Methods
    //======================================================================

    public ChannelStore getStore() {
        return mStore;
    }

    public static UseViewModel findViewModel(final Fragment fragment, Class<? extends UseViewModel> aClass) {
        return ViewModelProviders.of(fragment, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                try {
                    return modelClass.getDeclaredConstructor(Fragment.class).newInstance(fragment);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).get(aClass);
    }

    /**
     * {@link FragmentList#findFragment(int)}
     */
    public static Fragment findFragment(int key) {
        return sFragmentList.findFragment(key);
    }

    public boolean isFragmentActivated() {
        Fragment fragment = getFragment();
        return fragment.isAdded() && fragment.getUserVisibleHint();
    }

    public P getViewModel() {
        return mViewModel;
    }

    public void popBackStackImmediate() {
        boolean backPressed = mFragmentSupport.onBackPressed();
        Fragment fragment = getFragment();
        if (backPressed == true) {
            if (fragment.getFragmentManager().getBackStackEntryCount() > 0) {
                fragment.getFragmentManager().popBackStackImmediate();
            } else {
                ActivityCompat.finishAfterTransition(fragment.getActivity());
            }
        }
    }

    //======================================================================
    // Private Methods
    //======================================================================

    private static String makeLogMessage(String tag, String message) {
        if (!StringUtil.isEmpty(tag)) {
            return String.format(Locale.US, "[%s]: %s", tag, message);
        }
        return message;
    }

    @SuppressWarnings("unchecked")
    private void bindViewModel() {
        try {
            Fragment fragment = getFragment();
            Class<P> innerClass = (Class<P>) ClassUtil.getReclusiveGenericClass(fragment.getClass(), 0);
            mViewModel = (P) findViewModel(fragment, innerClass);
        } catch (Exception e) {
        }
    }

    private void cycle(String message) {
        Fragment fragment = getFragment();
        if (fragment == null) {
            return;
        }
        String objectName = fragment.getClass().getSimpleName();
    }
}
