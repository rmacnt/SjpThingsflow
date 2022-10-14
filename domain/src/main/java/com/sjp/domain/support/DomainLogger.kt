package com.sjp.domain.support

import android.util.Log
import java.util.*

object DomainLogger {
    private const val LOG_FORMAT = "[%s]: %s"

    private const val LOG_ENABLE = true

    fun i(tag: String?, message: String?) {
        if (LOG_ENABLE && message.isNullOrEmpty() == false) {
            Log.i(tag, message)
        }
    }

    fun d(tag: String?, message: String?) {
        if (LOG_ENABLE && message.isNullOrEmpty() == false) {
            Log.d(tag, message)
        }
    }

    fun e(tag: String?, message: String?) {
        if (LOG_ENABLE && message.isNullOrEmpty() == false) {
            Log.e(tag, message)
        }
    }

    fun w(tag: String?, message: String?) {
        if (LOG_ENABLE && message.isNullOrEmpty() == false) {
            Log.w(tag, message)
        }
    }

    fun v(tag: String?, message: String?) {
        if (LOG_ENABLE && message.isNullOrEmpty() == false) {
            Log.v(tag, message)
        }
    }

    fun i(tag: Tag, message: String?) {
        if (LOG_ENABLE && message.isNullOrEmpty() == false) {
            Log.i(tag.toString(), message)
        }
    }

    @JvmStatic
    fun d(tag: Tag, message: String?) {
        if (LOG_ENABLE && message.isNullOrEmpty() == false) {
            Log.d(tag.toString(), message)
        }
    }

    fun e(tag: Tag, message: String?) {
        if (LOG_ENABLE && message.isNullOrEmpty() == false) {
            Log.e(tag.toString(), message)
        }
    }

    fun w(tag: Tag, message: String?) {
        if (LOG_ENABLE && message.isNullOrEmpty() == false) {
            Log.w(tag.toString(), message)
        }
    }

    fun v(tag: Tag, message: String?) {
        if (LOG_ENABLE && message.isNullOrEmpty() == false) {
            Log.v(tag.toString(), message)
        }
    }

    fun printStackTrace(e: Exception) {
        if (LOG_ENABLE) {
            e.printStackTrace()
        }
    }

    fun makeLogMessage(tag: String?, message: String?): String {
        return String.format(Locale.US, LOG_FORMAT, tag, message)
    }

    fun makeLogMessage(tag: Class<*>?, message: String?): String? {
        return if (tag != null) {
            String.format(
                Locale.US,
                LOG_FORMAT,
                tag.simpleName,
                message
            )
        } else message
    }

    enum class Tag {
        DOMAIN
    }
}

fun DomainLogger.Tag.w(message: String?) {
    DomainLogger.w(this, message)
}

fun DomainLogger.Tag.d(message: String?) {
    DomainLogger.d(this, message)
}

fun DomainLogger.Tag.i(message: String?) {
    DomainLogger.i(this, message)
}

fun DomainLogger.Tag.e(message: String?) {
    DomainLogger.e(this, message)
}

fun DomainLogger.Tag.v(message: String?) {
    DomainLogger.v(this, message)
}