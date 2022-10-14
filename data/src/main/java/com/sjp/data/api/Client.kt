package com.sjp.data.api

import com.sjp.data.RetrofitFactory
import com.sjp.data.api.service.RepoService
import retrofit2.Retrofit

object Client {

    val baseUrl: String
    get() = "https://api.github.com/"


    private val api: Retrofit by lazy {
        RetrofitFactory.create(baseUrl)
    }

    val repo: RepoService
        get() {
            return api
                .newBuilder()
                .baseUrl(baseUrl)
                .build()
                .create(RepoService::class.java)
        }
}