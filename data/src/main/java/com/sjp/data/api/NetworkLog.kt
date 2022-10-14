package com.sjp.data.api

import android.text.TextUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

object NetworkLog {

    //======================================================================
    // Constants
    //======================================================================

    private const val FORMAT_INFO = "%s : %s"

    //======================================================================
    // Public Methods
    //======================================================================

    @JvmStatic
    fun printRequest(suffix: String = "", message: String) {
    }

    @JvmStatic
    fun printResponse(suffix: String = "", message: String) {
    }

    @JvmStatic
    fun printJson(suffix: String, json: String) {
        try {
            val depth = ArrayList<String>()

            printRequest(suffix, json)

            if (json.startsWith("[") && json.endsWith("]")) {
                printJsonArray(suffix, "complete", json, depth)
            } else if (json.startsWith("{") && json.endsWith("}")) {
                printJsonObject(suffix, json, depth)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //======================================================================
    // Private Methods
    //======================================================================

    @Throws(Exception::class)
    private fun printJsonArray(suffix: String, key: String, json: String, jsonDepth: List<String>) {
        try {
            val jsonArray = JSONArray(json)
            if (jsonArray.length() > 0) {
                for (i in 0 until jsonArray.length()) {
                    if (!jsonArray.isNull(i)) {
                        val temp = ArrayList<String>()
                        temp.addAll(jsonDepth)
                        temp.add("$key[$i]")

                        val `object` = jsonArray.get(i)

                        if (`object` is JSONObject) {
                            printJsonObject(
                                suffix,
                                `object`.toString(),
                                temp
                            )
                        } else if (`object` is JSONArray) {
                            printJsonArray(
                                suffix,
                                key,
                                `object`.toString(),
                                temp
                            )
                        } else {
                            printResponse(
                                suffix,
                                toJsonInfo(
                                    toJsonDepth(temp),
                                    `object`.toString()
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: OutOfMemoryError) {
            // Nothing
        }

    }

    @Throws(Exception::class)
    private fun printJsonObject(suffix: String, json: String, jsonDepth: List<String>) {
        try {
            val jsonObject = JSONObject(json)
            val iterator = jsonObject.keys()

            val tempJsonObject = JSONObject()

            while (iterator.hasNext()) {
                val key = iterator.next()
                val value = jsonObject.get(key)

                val temp = ArrayList<String>()
                temp.addAll(jsonDepth)

                if (jsonObject.isNull(key) == false) {
                    when (value) {
                        is JSONObject -> {
                            temp.add(key)
                            printJsonObject(
                                suffix,
                                value.toString(),
                                temp
                            )
                        }
                        is JSONArray -> printJsonArray(
                            suffix,
                            key,
                            value.toString(),
                            temp
                        )
                        else -> tempJsonObject.put(key, value)
                    }
                }
            }

            if (tempJsonObject.length() > 0) {
                printResponse(
                    suffix,
                    asString(
                        toJsonDepth(jsonDepth), tempJsonObject
                    )
                )
            }
        } catch (e: OutOfMemoryError) {
            // Nothing
        }
    }

    @Throws(JSONException::class)
    private fun asString(key: String, jsonObject: JSONObject): String {
        return try {
            String.format(Locale.US, FORMAT_INFO, key, jsonObject.toString(3))
        } catch (e: OutOfMemoryError) {
            String.format(Locale.US, FORMAT_INFO, key, "")
        }

    }

    @Throws(JSONException::class)
    private fun toJsonInfo(key: String, value: String): String {
        return try {
            String.format(Locale.US, FORMAT_INFO, key, value)
        } catch (e: OutOfMemoryError) {
            String.format(Locale.US, FORMAT_INFO, key, "")
        }

    }

    private fun toJsonDepth(depth: List<String>?): String {
        return if (depth != null && depth.isNotEmpty()) {
            TextUtils.join("-> ", depth)
        } else ""
    }
}
