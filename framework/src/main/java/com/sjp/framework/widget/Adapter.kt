package com.sjp.framework.widget

import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.sjp.framework.fragment.ViewBindingMapper
import com.sjp.framework.widget.utils.invokeDeclaredMethodsAnnotation

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.Target(AnnotationTarget.FUNCTION)
annotation class OnBindArguments

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.Target(AnnotationTarget.FUNCTION)
annotation class GetLifeCycleOwner

abstract class BaseRecyclerViewAdapter<Item : Any, VH : RecyclerViewHolder<Item>> :
    RecyclerView.Adapter<VH>() {

    abstract val holderSet: ViewHolderSet<Item>

    private val bundle: Bundle = Bundle()

    abstract fun getSupportItem(position: Int): Item?

    override fun onBindViewHolder(holder: VH, position: Int) {
        try {
            this.invokeDeclaredMethodsAnnotation(OnBindArguments::class.java, bundle)
            holder.onBindArguments(bundle)
        } catch (e: java.lang.Exception) {
        }
        try {
            val item = getSupportItem(position)
            holder.isPayloadHolderChange = false
            holder.onBindViewHolder(item)
        } catch (e: java.lang.Exception) {
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        try {
            this.invokeDeclaredMethodsAnnotation(OnBindArguments::class.java, bundle)
            holder.onBindArguments(bundle)
        } catch (e: java.lang.Exception) {
        }
        try {
            val item = getSupportItem(position)
            val payloadChange = payloads.find {
                it == "update"
            } != null
            holder.isPayloadHolderChange = payloadChange
            holder.onBindViewHolder(item)
            ViewBindingMapper.bind(holder)
        } catch (e: java.lang.Exception) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder = holderSet.createViewHolder(parent, viewType) as VH
        try {
            val owner = this.invokeDeclaredMethodsAnnotation(GetLifeCycleOwner::class.java)
            holder.onBindingLifeCycleScope(owner as LifecycleOwner?)
        } catch (e: java.lang.Exception) {
        }
        return holder
    }

    override fun getItemViewType(position: Int): Int {
        try {
            return holderSet.asViewType(getSupportItem(position))
        } catch (e: Exception) {
        }
        return super.getItemViewType(position)
    }

    override fun onViewRecycled(holder: VH) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
        ViewBindingMapper.unbind(holder)
    }

}
