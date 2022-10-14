package com.sjp.data.repository.base

import com.sjp.data.repository.base.datasource.CashDataSource
import com.sjp.data.repository.base.datasource.DatabaseDataSource
import com.sjp.data.repository.base.datasource.RemoteDataSource


abstract class Repository<Param, Result> {

    //======================================================================
    // Variables
    //======================================================================

    private val local: CashDataSource<Result>? by lazy {
        createCashDataSource()
    }

    private val remote: RemoteDataSource<Param, *, *> by lazy {
        createRemoteDataSource()
    }

    private val db: DatabaseDataSource<Param, Result>? by lazy {
        createDatabaseDataSource()
    }

    private var strategy: DataFetchStrategy = DataFetchStrategy.REMOTE

    private val dataSource: DataSource<Param, *>?
        get() {
            return when (strategy) {
                DataFetchStrategy.REMOTE -> {
                    remote
                }
                DataFetchStrategy.DB -> {
                    db
                }
                else -> null
            }
        }

    //======================================================================
    // Abstract Methods
    //======================================================================

    protected abstract fun createRemoteDataSource(): RemoteDataSource<Param, *, *>

    //======================================================================
    // Public Methods
    //======================================================================

    fun remote(): Repository<Param, Result> {
        strategy = DataFetchStrategy.REMOTE
        return this
    }

    fun local(): Repository<Param, Result> {
        strategy = DataFetchStrategy.LOCAL
        return this
    }

    @Deprecated("not implement")
    fun db(): Repository<Param, Result> {
        strategy = DataFetchStrategy.DB
        return this
    }

    protected open fun createCashDataSource(): CashDataSource<Result>? {
        return null
    }

    protected open fun createDatabaseDataSource(): DatabaseDataSource<Param, Result>? {
        return null
    }

    protected open fun toObject(raw: Any?): Result? {
        @Suppress("UNCHECKED_CAST")
        return raw as Result
    }

    fun responseStatus(value: ((code: Int, success: Boolean) -> Unit)?): Repository<Param, Result> {
        remote.responseStatus(value)
        return this
    }

    fun response(value: ((Result?) -> Unit)?): Repository<Param, Result> {
        val source = dataSource
        if (source != null) {
            source.response {
                saveCash(it)
                value?.let { raw ->
                    raw(toObject(it))
                }
                onDestroy()
            }
        } else {
            value?.let { raw ->
                raw(toObject(local?.get()))
                onDestroy()
            }
        }
        return this
    }

    fun fail(value: ((error: Throwable) -> Unit)?): Repository<Param, Result> {
        dataSource?.fail {
            clearCash()
            value?.let { raw ->
                raw(it)
            }
            onDestroy()
        }
        return this
    }

    fun parameter(p: Param): Repository<Param, Result> {
        dataSource?.parameter(p)
        return this
    }

    protected open fun onDestroy() {

    }

    fun execute() {
        dataSource?.execute()
    }

    fun sync(): Result? {
        return if (strategy == DataFetchStrategy.REMOTE || strategy == DataFetchStrategy.DB) {
            val s = when (strategy) {
                DataFetchStrategy.REMOTE -> {
                    remote
                }
                DataFetchStrategy.DB -> {
                    db
                }
                else -> {
                    null
                }
            }
            s?.let {
                val item = it.sync()
                onDestroy()
                saveCash(item)
                item
            }
        } else {
            local?.get()
        }?.let {
            toObject(it)
        }
    }

    fun clearCash() {
        local?.clear()
    }

    fun saveCash(value: Any?) {
        local?.let {
            it.clear()
            it.save(value)
        }
    }

    //======================================================================
    // DataFetchStrategy
    //======================================================================

    enum class DataFetchStrategy {
        REMOTE,
        LOCAL,
        DB
    }
}

open class EmptyParameter