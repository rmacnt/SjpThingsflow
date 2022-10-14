package com.sjp.data.api.call

import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResponseFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        return if (rawType == ApiCall::class.java) {
            val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
            ResponseCallAdapter<Any>(
                async = false,
                responseType = observableType
            )
        } else {
            ResponseCallAdapter<Any>(async = true, responseType = rawType)
        }
    }

    companion object {

        fun create(): ResponseFactory {
            return ResponseFactory()
        }
    }
}