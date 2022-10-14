package com.sjp.framework.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

class BaseRecyclerView : RecyclerView {

    //======================================================================
    // Variables
    //======================================================================

    private var layoutMediator: LayoutMediator? = null

    var disableTouchEvent: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        androidx.recyclerview.R.attr.recyclerViewStyle
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    //======================================================================
    // Override Methods
    //======================================================================

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (disableTouchEvent) {
            return false
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return super.canScrollVertically(direction)
    }

    //======================================================================
    // Public Methods
    //======================================================================


    fun setLayoutMediator(value: LayoutMediator) {
        if (value != layoutMediator) {
            adapter = value.adapter

            value.layoutManager = null
            layoutManager = value.onCreateLayoutManager()
            value.layoutManager = layoutManager

            value.itemDecoration?.run {
                addItemDecoration(this)
            }
            value.snapper?.run {
                attachToRecyclerView(this@BaseRecyclerView)
            }
            value.onCreateScrollListener()?.run {
                addOnScrollListener(this)
            }
            layoutMediator = value
        }
    }


    //======================================================================
    // LayoutMediator
    //======================================================================

    abstract class LayoutMediator {

        //======================================================================
        // Variables
        //======================================================================

        val adapter: Adapter<out ViewHolder> by lazy {
            onCreateAdapter()
        }

        var layoutManager: LayoutManager? = null
            internal set

        val itemDecoration: ItemDecoration? by lazy {
            onCreateItemDecoration()
        }

        val snapper: SnapHelper? by lazy {
            onCreateSnapHelper()
        }

        //======================================================================
        // Abstract Methods
        //======================================================================

        abstract fun onCreateAdapter(): Adapter<out ViewHolder>

        abstract fun onCreateLayoutManager(): LayoutManager

        //======================================================================
        // Public Methods
        //======================================================================

        open fun onCreateItemDecoration(): ItemDecoration? {
            return null
        }

        open fun onCreateSnapHelper(): SnapHelper? {
            return null
        }

        open fun onCreateScrollListener(): OnScrollListener? {
            return null
        }

    }
}