package com.sjp.framework.fragment

import android.app.Activity
import android.app.Application
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider

@Deprecated("google code Deprecated")
object ViewModelProviders {

    //======================================================================
    // Public Methods
    //======================================================================

    @MainThread
    @JvmStatic
    fun of(fragment: Fragment): ViewModelProvider {
        return of(fragment, null)
    }

    @MainThread
    @JvmStatic
    fun of(activity: FragmentActivity): ViewModelProvider {
        return of(activity, null)
    }

    @MainThread
    @JvmStatic
    fun of(fragment: Fragment, factory: ViewModelProvider.Factory?): ViewModelProvider {
        var factory = factory
        val application = checkApplication(checkActivity(fragment))
        if (factory == null) {
            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        }
        return ViewModelProvider(fragment.viewModelStore, factory)
    }

    @MainThread
    @JvmStatic
    fun of(
        activity: FragmentActivity,
        factory: ViewModelProvider.Factory?
    ): ViewModelProvider {
        var factory = factory
        val application = checkApplication(activity)
        if (factory == null) {
            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        }
        return ViewModelProvider(activity.viewModelStore, factory)
    }

    //======================================================================
    // Private Methods
    //======================================================================

    private fun checkApplication(activity: Activity): Application {
        return activity.application
            ?: throw IllegalStateException(
                "Your activity/fragment is not yet attached to "
                        + "Application. You can't request ViewModel before onCreate call."
            )
    }

    private fun checkActivity(fragment: Fragment): Activity {
        return fragment.activity
            ?: throw IllegalStateException("Can't create ViewModelProvider for detached fragment")
    }

    //======================================================================
    // DefaultFactory
    //======================================================================

    @Deprecated("Use {@link ViewModelProvider.AndroidViewModelFactory}")
    class DefaultFactory
    @Deprecated(
        """Use {@link ViewModelProvider.AndroidViewModelFactory} or
          {@link ViewModelProvider.AndroidViewModelFactory#getInstance(Application)}."""
    ) constructor(application: Application) : ViewModelProvider.AndroidViewModelFactory(application)
}