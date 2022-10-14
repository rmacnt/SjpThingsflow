package com.sjp.data.repository.base

abstract class DataSource<Param, Result> {

    //======================================================================
    // Variables
    //======================================================================

    internal var responseListener: ((Result?) -> Unit)? = null

    internal var errorListener: ((error: Throwable) -> Unit)? = null

    internal var param: Param? = null

    fun response(value: ((Result?) -> Unit)?): DataSource<Param, Result> {
        responseListener = value
        return this
    }

    fun fail(value: ((error: Throwable) -> Unit)?): DataSource<Param, Result> {
        errorListener = value
        return this
    }

    fun parameter(p: Param): DataSource<Param, Result> {
        param = p
        return this
    }

    //======================================================================
    // Abstract Methods
    //======================================================================

    abstract fun execute()

    abstract fun sync(): Result?

    //======================================================================
    // Override Methods
    //======================================================================

    open fun onDestroy() {
        responseListener = null
        errorListener = null
        param = null
    }
}


