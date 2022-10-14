package com.sjp.data.api.service

import com.sjp.data.api.call.ApiCall
import com.sjp.data.api.entity.repo.RepoEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface RepoService {
    @GET("repos/{org}/{repo}/issues")
    fun getRepo(
        @Path("org") org : String,
        @Path("repo") repo : String
    ): ApiCall<ArrayList<RepoEntity>>

}