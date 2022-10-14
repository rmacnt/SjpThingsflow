package com.sjp.data.repository.base.datasource

import com.sjp.data.api.call.ApiCall
import com.sjp.data.nonnull
import com.sjp.data.repository.base.DataSource
import com.sjp.data.api.DataLogger
import com.sjp.data.repository.base.datasource.exception.ClientInternalErrorException
import com.sjp.data.repository.base.datasource.exception.NetworkConnectionErrorException
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException

@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
abstract class RemoteDataSource<Param, Result, RawResponse> :
    DataSource<Param, Result>() {

    //======================================================================
    // Variables
    //======================================================================

    private var responseDebugInfoListener: ((statusCode: Int, success: Boolean, debugMessage: String) -> Unit)? =
        null

    private var responseStatusListener: ((statusCode: Int, success: Boolean) -> Unit)? =
        null

    //======================================================================
    // Abstract Methods
    //======================================================================

    protected abstract fun fetchApi(param: Param?): ApiCall<RawResponse>

    abstract fun toObject(raw: RawResponse?): Result?

    //======================================================================
    // Override Methods
    //======================================================================

    override fun execute() {
        fun destroy() {
            onDestroy()
            /*responseStatus.reset()*/
        }

        val api = fetchApi(this.param)
        api.response { code, raw ->
            try {
                successCheck(code, raw)
                onResponse(raw)
                val asResponse = toObjectInternal(raw)
                responseListener?.let { re ->
                    re(asResponse)
                }
            } catch (e: Exception) {
                DataLogger.printStackTrace(e)
                dispatchError(e)
            } finally {
                destroy()
            }
        }.failure { e ->
            DataLogger.printStackTrace(e)
            dispatchError(e)
            destroy()
        }.execute()
    }

    override fun sync(): Result? {
        try {
            val item = fetchApi(param).async()
            val code = item?.code.nonnull()
            val rawResponse = item?.body
            successCheck(code, rawResponse)

            return toObjectInternal(rawResponse)
        } catch (e: Exception) {
            dispatchError(e)
        } finally {
            onDestroy()
        }
        return null
    }

    //======================================================================
    // Public Methods
    //======================================================================


    fun responseStatus(value: ((statusCode: Int, success: Boolean) -> Unit)?): DataSource<Param, Result> {
        responseStatusListener = value
        return this
    }

    fun responseDebugInfo(value: ((statusCode: Int, success: Boolean, message: String) -> Unit)?): DataSource<Param, Result> {
        responseDebugInfoListener = value
        return this
    }

    open fun onResponse(response: RawResponse?) {
    }

    //======================================================================
    // Private Methods
    //======================================================================

    @Throws(IOException::class)
    private fun toObjectInternal(raw: RawResponse?): Result? {
        @Suppress("UNREACHABLE_CODE")
        return try {
            return toObject(raw)
        } catch (e: Exception) {
            throw ClientInternalErrorException(e.message)
        }
    }

    @Throws(IOException::class)
    private fun successCheck(code: Int, response: RawResponse?) {
        val result = when {
            response is ArrayList<*> -> {
                if (response.isNotEmpty()) {
                    isResultSuccess(response.size)
                } else {
                    true
                }
            }
            response?.isAccessTokenEntity() == true -> {
                true
            }
            else -> {
                false
            }
        }
        log(
            "successCheck >> result : ${result}, response : ${response}"
        )
        responseStatusListener?.run {
            this(code, result)
        }

        if (!result) {
            throw IOException()
        } else {
            responseDebugInfoListener?.run {
                val status = response?.toString().orEmpty()
                this(code, result, status)
            }
        }
    }

    private fun dispatchError(error: Throwable) {
        logE("dispatchError >> $error")
        errorListener?.run {
            if (error is UnknownHostException) {
                this(NetworkConnectionErrorException(error.message))
            }
        }
        responseDebugInfoListener?.run {
            val sw = StringWriter()
            error.printStackTrace(PrintWriter(sw))
            this(-1, false, "$sw")
        }
    }

    private fun isResultSuccess(result: Int): Boolean {
        return result > 0
        }
    }

    private fun log(message: String) {
        DataLogger.w(
            DataLogger.Tag.DATA_REQUEST,
            "[${RemoteDataSource::class.java.simpleName}] $message"
        )
    }

    private fun logE(message: String) {
        DataLogger.e(
            DataLogger.Tag.DATA_REQUEST,
            "[${RemoteDataSource::class.java.simpleName}] $message"
        )
    }


private fun Any.isAccessTokenEntity(): Boolean {
    val response = this@isAccessTokenEntity
    val token = response::class.java.getAnnotation(AccessTokenEntity::class.java)
    DataLogger.w(
        DataLogger.Tag.DATA_REQUEST,
        "token >> $token >> ${token != null}"
    )
    return token != null
}