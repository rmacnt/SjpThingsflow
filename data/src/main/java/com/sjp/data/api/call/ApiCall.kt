package com.sjp.data.api.call

import com.sjp.data.nonnull
import com.sjp.data.api.DataLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class ApiCall<R>(
    private val originalCall: Call<R>
) {

    //======================================================================
    // Variables
    //======================================================================

    private var responseListener: ((code: Int, r: R?) -> Unit)? = null

    private var failureListener: ((e: Throwable) -> Unit)? = null

    private val uiScope = CoroutineScope(Dispatchers.Main)

    //======================================================================
    // Public Methods
    //======================================================================

    fun failure(l: (e: Throwable) -> Unit): ApiCall<R> {
        failureListener = l
        return this
    }

    fun response(r: (code: Int, raw: R?) -> Unit): ApiCall<R> {
        responseListener = r
        return this
    }

    fun async(): ApiResponseWrapper<R>? {
        try {
            val response = originalCall.execute()
            val body = response.body()
            DataLogger.d(
                "${DataLogger.Tag.DATA_RESPONSE}[${originalCall.request().url.encodedPath}]",
                "onBody call key : ${hashCode()} message : $body"
            )
            return ApiResponseWrapper(response.code().nonnull(), response.message().orEmpty(), body)
        } catch (e: Exception) {
            DataLogger.e(
                "${DataLogger.Tag.DATA_RESPONSE}[${originalCall.request().url.encodedPath}]",
                "onFailure call key : ${hashCode()} message : $e"
            )
        }
        return null
    }

    fun execute() {
        fun release() {
            failureListener = null
            responseListener = null
            DataLogger.d(DataLogger.Tag.DATA_RESPONSE, "release ${hashCode()}")
        }

        val c = originalCall.clone()
        c.enqueue(object : retrofit2.Callback<R> {
            override fun onFailure(call: Call<R>, t: Throwable) {
                DataLogger.e(
                    "${DataLogger.Tag.DATA_RESPONSE}[${call.request().url.encodedPath}]ApiCall",
                    "onFailure call key : ${hashCode()} message : $t"
                )
                failureListener?.let {
                    uiScope.launch {
                        it(t)
                        release()
                    }
                }.run {
                    release()
                }
            }

            override fun onResponse(call: Call<R>, response: Response<R>) {
                DataLogger.w(
                    "${DataLogger.Tag.DATA_RESPONSE}[${call.request().url.encodedPath}]ApiCall",
                    "onResponse call key : ${hashCode()}"
                )
                responseListener?.let {
                    uiScope.launch{
                        it(response.code(), response.body())
                        release()
                    }
                }.run {
                    release()
                }
            }
        })
    }

    data class ApiResponseWrapper<R>(val code: Int, val message: String, val body: R?)
}

