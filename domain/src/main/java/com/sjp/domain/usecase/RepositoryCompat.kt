package com.sjp.domain.usecase

import com.sjp.domain.support.DomainLogger
import com.sjp.data.repository.base.Repository

abstract class RepositoryCompat<Repo : Repository<*, RawResult>, Parameter, DomainResult, RawResult> {

    //======================================================================
    // Private Variables
    //======================================================================

    private val repository: Repo by lazy {
        val innerClass = ClassUtil.getReclusiveGenericClass(this::class.java, 0) as Class<*>
        innerClass.newInstance() as Repo
    }

    //======================================================================
    // Public Methods
    //======================================================================

    open fun toObject(raw: RawResult?): DomainResult? {
        @Suppress("UNCHECKED_CAST")
        return raw as DomainResult
    }

    fun remote(): RepositoryCompat<Repo, Parameter, DomainResult, RawResult> {
        repository.remote()
        return this
    }

    fun local(): RepositoryCompat<Repo, Parameter, DomainResult, RawResult> {
        repository.local()
        return this
    }

    fun clearCash() {
//        repository.clearCache()
    }

    fun saveCash(value: RawResult?) {
//        repository.saveCache(value)
    }

    fun responseStatus(value: ((code: Int, success: Boolean) -> Unit)?): RepositoryCompat<Repo, Parameter, DomainResult, RawResult> {
        repository.responseStatus(value)
        return this
    }

    fun responseDebugInfo(value: ((code: Int, success: Boolean, message: String) -> Unit)?): RepositoryCompat<Repo, Parameter, DomainResult, RawResult> {
//        repository.responseDebugInfo(value)
        return this
    }

    fun success(value: ((DomainResult?) -> Unit)?): RepositoryCompat<Repo, Parameter, DomainResult, RawResult> {
        repository.response {
            value?.let { raw ->
                val to = toObject(it)
                raw(to)
                onDestroy()
            }
        }
        return this
    }

    fun fail(value: ((error: Throwable) -> Unit)?): RepositoryCompat<Repo, Parameter, DomainResult, RawResult> {
        repository.fail {
            value?.let { raw ->
                raw(it)
                onDestroy()
            }
        }
        return this
    }

    fun parameter(p: Parameter): RepositoryCompat<Repo, Parameter, DomainResult, RawResult> {
        Helper.applyParameter(repository, p)
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun parameter2(init: Parameter.() -> Unit): RepositoryCompat<Repo, Parameter, DomainResult, RawResult> {
        val p: Parameter =
            ClassUtil.getReclusiveGenericClass(this::class.java, 1).newInstance() as Parameter
        p?.init()
        Helper.applyParameter(repository, p)
        return this
    }

    fun execute() {
        try {
            onStart()
            repository.execute()
        } catch (e: Exception) {
            DomainLogger.printStackTrace(e)
        }
    }

    fun sync(): DomainResult? {
        try {
            onStart()
            return repository.let {
                val r = it.sync()
                r
            }.let {
                val to = toObject(it)
                onDestroy()
                to
            }
        } catch (e: Exception) {
            DomainLogger.printStackTrace(e)
        }
        return null
    }

    //======================================================================
    // Private Methods
    //======================================================================

    @Throws(IllegalArgumentException::class)
    private fun onStart() {
        // Nothing
    }

    private fun onDestroy() {
        // Nothing
    }
}

