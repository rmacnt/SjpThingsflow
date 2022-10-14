package com.sjp.framework.fragment

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.sjp.framework.widget.RecyclerViewHolder


//======================================================================
// Binding
//======================================================================

object ViewBindingMapper {

    @JvmName("bind")
    fun bind(target: Any, view: View, model: Any) {
        bindInternal(target, view, model)
    }

    @JvmName("bind")
    fun bind(fragment: Fragment, model: Any) {
        fragment.view?.let { bindInternal(fragment, it, model) }
    }

    @JvmName("bind")
    fun bind(holder: RecyclerViewHolder<*>) {
        val viewModel = findViewMode(holder)
        viewModel?.let { bindInternal(holder, holder.itemView, it) }
    }

    @JvmName("unbind")
    fun unbind(holder: RecyclerViewHolder<*>) {
        unbindInternal(holder)
    }

    @JvmName("unbind")
    fun unbind(fragment: Fragment) {
        unbindInternal(fragment)
    }

    @JvmName("unbind")
    fun unbind(target: Any) {
        unbindInternal(target)
    }

    private fun bindInternal(target: Any, view: View, model: Any) {
        target::class.java.declaredFields.forEach { f ->
            try {
                val tag = f.getAnnotation(ViewBindingModel::class.java)
                if (tag != null) {
                    val binding = DataBindingUtil.bind<ViewDataBinding>(view)
                    try {
                        if (binding != null) {
                            if (target is LifecycleOwner) {
                                binding.lifecycleOwner = target
                            }
                            binding.setVariable(tag.modelId, model)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (f.isAccessible == false) {
                        f.isAccessible = true
                    }
                    f?.set(target, binding)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun unbindInternal(target: Any) {
        target::class.java.declaredFields.forEach { f ->
            try {
                val tag = f.getAnnotation(ViewBindingModel::class.java)
                if (tag != null) {
                    if (f.isAccessible.not()) {
                        f.isAccessible = true
                    }
                    val value = f.get(target)
                    if (value is ViewDataBinding) {
                        value.unbind()
                        f.set(target, null)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun findViewMode(target: Any): ViewBindingModel? {
        target::class.java.declaredFields.forEach { f ->
            try {
                val tag = f.getAnnotation(ViewBindingModel::class.java)
                if (tag != null) {
                    return tag
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }
}