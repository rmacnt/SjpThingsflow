package com.sjp.framework.fragment;

import com.sjp.framework.channel.ChannelStore;

public interface FragmentSupport<P extends UseViewModel> {

    @Deprecated
    P getPresenter();

    P getViewmodel();

    ChannelStore getChannelStore();

    boolean onBackPressed();

    boolean isFragmentActivated();

    void popBackStackImmediate();
}
