package com.sjp.data.repository.base.datasource

import com.orhanobut.hawk.Hawk

abstract class CashDataSource<Result> {

    //======================================================================
    // Variables
    //======================================================================

    abstract val key: String

    val cacheContains: Boolean
        get() = Hawk.contains(key)

    //======================================================================
    // Public Methods
    //======================================================================

    fun save(prefix: String, value: Any?) {
        Hawk.put("$key:$prefix", value)
    }

    fun save(value: Any?) {
        Hawk.put(key, value)
    }


    fun get(): Result? {
        try {

            val get: Result = Hawk.get(key)
            return get
        } catch (e: Exception) {
        }
        return null
    }

    fun get(prefix: String): Result? {
        try {
            val k = "$key:$prefix"
            val get: Result = Hawk.get(k)
            return get
        } catch (e: Exception) {
        }
        return null
    }

    fun clear() {
        Hawk.delete(key)
    }
}