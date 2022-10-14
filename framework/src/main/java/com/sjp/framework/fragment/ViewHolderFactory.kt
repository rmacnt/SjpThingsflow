package com.sjp.framework.fragment

import android.view.ViewGroup
import com.sjp.framework.widget.RecyclerViewHolder

object ViewHolderFactory {

    fun create(
        viewHolderClass: Class<*>,
        viewGroup: ViewGroup
    ): RecyclerViewHolder<*>? {
        try {
            return viewHolderClass.getDeclaredConstructor(
                ViewGroup::class.java
            ).newInstance(
                viewGroup
            ) as RecyclerViewHolder<*>
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}