package com.sjp.domain


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

internal object GsonLoader {
    val gson: Gson by lazy {
        GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create()
    }
}

fun <T> T.toJson(): String {
    return GsonLoader.gson.toJson(this)
}

fun <T> String.fromJson(type: Class<T>): T {
    return GsonLoader.gson.fromJson(this, type)
}

fun <T> String.fromJson(type: TypeToken<T>): T {
    return GsonLoader.gson.fromJson(this, type.type)
}

val gson
    get() = GsonLoader.gson