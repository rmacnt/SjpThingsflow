package com.sjp.domain.usecase

import com.sjp.data.repository.base.Repository
import com.sjp.domain.support.DomainLogger.printStackTrace

abstract class UseCase<Repo : Repository<*, RawResult>, Parameter, DomainResult, RawResult> {

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

    fun remote(): UseCase<Repo, Parameter, DomainResult, RawResult> {
        repository.remote()
        return this
    }

    fun local(): UseCase<Repo, Parameter, DomainResult, RawResult> {
        repository.local()
        return this
    }

    fun clearCash() {
        repository.clearCash()
    }

    fun saveCash(value: RawResult?) {
        repository.saveCash(value)
    }

    fun responseStatus(value: ((code: Int, success: Boolean) -> Unit)?): UseCase<Repo, Parameter, DomainResult, RawResult> {
        repository.responseStatus(value)
        return this
    }

    fun success(value: ((DomainResult?) -> Unit)?): UseCase<Repo, Parameter, DomainResult, RawResult> {
        repository.response {
            value?.let { raw ->
                val to = toObject(it)
                raw(to)
                onDestroy()
            }
        }
        return this
    }

    fun fail(value: ((error: Throwable) -> Unit)?): UseCase<Repo, Parameter, DomainResult, RawResult> {
        repository.fail {
            value?.let { raw ->
                raw(it)
                onDestroy()
            }
        }
        return this
    }

    fun parameter(p: Parameter): UseCase<Repo, Parameter, DomainResult, RawResult> {
        Helper.applyParameter(repository, p)
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun parameter2(init: Parameter.() -> Unit): UseCase<Repo, Parameter, DomainResult, RawResult> {
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
            printStackTrace(e)
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
            printStackTrace(e)
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

