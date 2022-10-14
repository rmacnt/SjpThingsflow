package com.sjp.framework.channel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*

class ChannelLiveData<T> {

    val value: T?
        get() = bus?.value

    init {

    }

    private var bus: MutableLiveData<T>? = MutableLiveData()

    private var lifeCycle: Lifecycle? = null

    fun observe(fragment: Fragment, event: (value: T?) -> Unit) {
        if (bus == null) {
            bus = MutableLiveData()
        }

        if (lifeCycle == null) {
            lifeCycle = fragment.lifecycle
            lifeCycle?.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestory() {
                    lifeCycle?.removeObserver(this)
                    lifeCycle = null
                    bus = null
                }
            })
        }
        bus?.observe(fragment, Observer {
            event(it)
        })
    }

    fun observe(owner: FragmentActivity, event: (value: T?) -> Unit) {
        if (bus == null) {
            bus = MutableLiveData()
        }
        if (lifeCycle == null) {
            lifeCycle = owner.lifecycle
            lifeCycle?.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestory() {
                    lifeCycle?.removeObserver(this)
                    lifeCycle = null
                    bus = null
                }
            })
        }
        bus?.observe(owner, Observer {
            event(it)
        })
    }

    fun postValue(event: T?) {
        if (bus == null) {
            bus = MutableLiveData()
        }
        bus?.postValue(event)
    }

    fun onDestroy() {
        bus = null
        lifeCycle = null
    }

}