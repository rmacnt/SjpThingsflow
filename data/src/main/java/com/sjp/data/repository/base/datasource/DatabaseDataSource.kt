package com.sjp.data.repository.base.datasource

import com.sjp.data.repository.base.DataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
abstract class DatabaseDataSource<Param, Result> :
    DataSource<Param, Result>() {

    //======================================================================
    // Variables
    //======================================================================

    private var scope: CoroutineScope? = null

    //======================================================================
    // Abstract Methods
    //======================================================================

    protected abstract fun fetch(param: Param?): Result

    //======================================================================
    // Override Methods
    //======================================================================

    override fun execute() {
        getService()?.launch {
            try {
                val dao = fetch(param)
                launch(Dispatchers.Main) {
                    responseListener?.let { re ->
                        re(dao)
                    }
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    errorListener?.let { re ->
                        re(e)
                    }
                }
            } finally {
                onDestroy()
            }
        }
    }

    override fun sync(): Result? {
        try {
            return fetch(param)
        } catch (e: java.lang.Exception) {
            errorListener?.invoke(e)
        } finally {
            onDestroy()
        }
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        scope = null
    }

    //======================================================================
    // Private Methods
    //======================================================================

    private fun getService(): CoroutineScope? {
        if (scope == null) {
            scope = CoroutineScope(Dispatchers.IO)
        }
        return scope
    }
}