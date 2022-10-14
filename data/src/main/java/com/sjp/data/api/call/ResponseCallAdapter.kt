package com.sjp.data.api.call

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type


class ResponseCallAdapter<R>(val async: Boolean, val responseType: Type) : CallAdapter<R, Any> {

    override fun adapt(call: Call<R>): Any? {
        if (async == true) {
            try {
                return call.execute().body()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        } else {
            return ApiCall(call)
        }
    }

    override fun responseType(): Type {
        return responseType
    }
}