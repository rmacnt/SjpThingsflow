package com.sjp.framework.widget


import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.lifecycle.Lifecycle
import androidx.paging.*
import androidx.paging.LoadState.NotLoading
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.ALLOW
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT
import com.sjp.framework.fragment.ViewBindingMapper
import com.sjp.framework.widget.utils.invokeDeclaredMethodsAnnotation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

abstract class RawPagingDataAdapter<T : Any, VH : RecyclerView.ViewHolder> @JvmOverloads constructor(
    diffCallback: DiffUtil.ItemCallback<T>,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default
) : RecyclerView.Adapter<VH>() {

    private var userSetRestorationPolicy = false

    override fun setStateRestorationPolicy(strategy: StateRestorationPolicy) {
        userSetRestorationPolicy = true
        super.setStateRestorationPolicy(strategy)
    }

    private val differ = AsyncPagingDataDiffer(
        diffCallback = diffCallback,
        updateCallback = AdapterListUpdateCallback(this),
        mainDispatcher = mainDispatcher,
        workerDispatcher = workerDispatcher
    )

    init {
        super.setStateRestorationPolicy(PREVENT)

        fun considerAllowingStateRestoration() {
            if (stateRestorationPolicy == PREVENT && !userSetRestorationPolicy) {
                this@RawPagingDataAdapter.stateRestorationPolicy = ALLOW
            }
        }

        @Suppress("LeakingThis")
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                considerAllowingStateRestoration()
                unregisterAdapterDataObserver(this)
                super.onItemRangeInserted(positionStart, itemCount)
            }
        })

        addLoadStateListener(object : Function1<CombinedLoadStates, Unit> {
            private var ignoreNextEvent = true

            override fun invoke(loadStates: CombinedLoadStates) {
                if (ignoreNextEvent) {
                    ignoreNextEvent = false
                } else if (loadStates.source.refresh is NotLoading) {
                    considerAllowingStateRestoration()
                    removeLoadStateListener(this)
                }
            }
        })
    }

    val pagedItemCount: Int
        get() = differ.itemCount

    final override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    final override fun setHasStableIds(hasStableIds: Boolean) {
        throw UnsupportedOperationException("Stable ids are unsupported on PagingDataAdapter.")
    }

    suspend fun submitData(pagingData: PagingData<T>) {
        differ.submitData(pagingData)
    }

    fun submitData(lifecycle: Lifecycle, pagingData: PagingData<T>) {
        differ.submitData(lifecycle, pagingData)
    }

    fun retry() {
        differ.retry()
    }

    fun refresh() {
        differ.refresh()
    }

    protected fun getItem(@IntRange(from = 0) position: Int) = differ.getItem(position)

    fun peek(@IntRange(from = 0) index: Int) = differ.peek(index)

    fun snapshot(): ItemSnapshotList<T> = differ.snapshot()

    override fun getItemCount() = differ.itemCount

    val loadStateFlow: Flow<CombinedLoadStates> = differ.loadStateFlow

    fun addLoadStateListener(listener: (CombinedLoadStates) -> Unit) {
        differ.addLoadStateListener(listener)
    }
    fun removeLoadStateListener(listener: (CombinedLoadStates) -> Unit) {
        differ.removeLoadStateListener(listener)
    }

    fun withLoadStateHeader(
        header: LoadStateAdapter<*>
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            header.loadState = loadStates.prepend
        }
        return ConcatAdapter(header, this)
    }

    fun withLoadStateFooter(
        footer: LoadStateAdapter<*>
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            footer.loadState = loadStates.append
        }
        return ConcatAdapter(this, footer)
    }

    fun withLoadStateHeaderAndFooter(
        header: LoadStateAdapter<*>,
        footer: LoadStateAdapter<*>
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            header.loadState = loadStates.prepend
            footer.loadState = loadStates.append
        }
        return ConcatAdapter(header, this, footer)
    }
}


abstract class BasePagingDataAdapter<Item : Any, VH : RecyclerViewHolder<Item>>(diffCallback: DiffUtil.ItemCallback<Item>) :
    RawPagingDataAdapter<Item, VH>(diffCallback) {

    //======================================================================
    // Abstract Variables
    //======================================================================

    abstract val holderSet: ViewHolderSet<Item>

    private val bundle: Bundle = Bundle()

    //======================================================================
    // Override Methods
    //======================================================================

    override fun onBindViewHolder(holder: VH, position: Int) {
        try {
            this.invokeDeclaredMethodsAnnotation(OnBindArguments::class.java, bundle)
            holder.onBindArguments(bundle)
        } catch (e: java.lang.Exception) {
        }
        try {
            val item: Item? = getSupportItem(position)
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
        return holderSet.createViewHolder(parent, viewType) as VH
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
    }

    //======================================================================
    // Public Methods
    //======================================================================

    open fun getSupportItem(position: Int): Item? {
        try {
            return getItem(position)
        } catch (e: Exception) {
        }
        return null
    }
}
