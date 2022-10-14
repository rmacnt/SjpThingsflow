package com.sjp.framework.channel

import androidx.fragment.app.Fragment
import com.sjp.framework.fragment.FragmentSupport

object ChannelProviders {

    @Deprecated("bug")
    val global: ChannelProvider by lazy {
        ChannelProvider(ChannelStore())
    }

    fun of(fragment: Fragment?): ChannelProvider? {
        if (fragment is FragmentSupport<*>) {
            return ChannelProvider(fragment.channelStore)
        }
        return null
    }

}