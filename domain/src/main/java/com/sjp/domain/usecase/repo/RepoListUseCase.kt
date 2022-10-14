package com.sjp.domain.usecase.repo

import com.sjp.data.GsonLoader.gson
import com.sjp.data.api.entity.repo.RepoEntity
import com.sjp.data.repository.repo.RepoListRepository
import com.sjp.domain.repo.Repo
import com.sjp.domain.toJson
import com.sjp.domain.usecase.UseCase
import com.google.gson.reflect.TypeToken

class RepoListUseCase :
    UseCase<RepoListRepository, RepoListRepository.Parameter, ArrayList<Repo>, ArrayList<RepoEntity>>() {

    //======================================================================
    // Override Methods
    //======================================================================

    override fun toObject(raw: ArrayList<RepoEntity>?): ArrayList<Repo>? {
        return raw?.let {
            gson.fromJson(
                it.toJson(),
                object : TypeToken<ArrayList<Repo>>() {}.type
            )
        }
    }

}