package com.sjp.framework.fragment;


import android.os.Bundle;
import android.util.SparseArray;

import androidx.fragment.app.Fragment;

public final class FragmentList {

    //======================================================================
    // Variables
    //======================================================================

    private final SparseArray<Fragment> mFragments = new SparseArray<>();

    //======================================================================
    // Constructor
    //======================================================================

    public FragmentList() {
        // Nothing
    }

    //======================================================================
    // Public Methods
    //======================================================================

    /**
     * 저장 된 {@link Fragment} 가져 온다.
     *
     * @param key {@link Fragment#hashCode()}
     *
     * @return {@link Fragment}
     */
    public final Fragment findFragment(int key) {
        return getActivatedFragmentInternal(key);
    }

    /**
     * {@link Fragment} Cycle 에 맞추서 해제 되기 때문에 해제 처리가 필요 없다.
     */
    @Deprecated
    public void release() {
        mFragments.clear();
    }

    //======================================================================
    // Methods
    //======================================================================

    /**
     * {@link Fragment#onCreate(Bundle)} } 호출시 사용
     *
     * @param fragment 저장 {@link Fragment}
     */
    void onCreate(Fragment fragment) {
        mFragments.put(fragment.hashCode(), fragment);
    }

    /**
     * {@link Fragment#onDestroy()}} 호출시 사용
     *
     * @param fragment 저장 {@link Fragment}
     */
    void onDestroy(Fragment fragment) {
        mFragments.remove(fragment.hashCode());
    }

    //======================================================================
    // Private Methods
    //======================================================================

    private Fragment getActivatedFragmentInternal(int key) {
        return mFragments.get(key);
    }
}
