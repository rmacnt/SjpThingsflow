package com.sjp.data.repository.repo

import com.sjp.data.api.Client
import com.sjp.data.api.call.ApiCall
import com.sjp.data.api.entity.repo.RepoEntity
import com.sjp.data.nonnull
import com.sjp.data.repository.base.Repository
import com.sjp.data.repository.base.datasource.RemoteDataSource

class RepoListRepository:
    Repository<RepoListRepository.Parameter, ArrayList<RepoEntity>>() {

    //======================================================================
    // Override Methods
    //======================================================================

    override fun createRemoteDataSource(): RemoteDataSource<Parameter, *, *> {
        return object :
            RemoteDataSource<Parameter, ArrayList<RepoEntity>, ArrayList<RepoEntity>>() {
            override fun fetchApi(param: Parameter?): ApiCall<ArrayList<RepoEntity>> {
                return Client.repo.getRepo(
                    org = param?.org.nonnull(),
                    repo = param?.repo.nonnull(),
                )
            }

            override fun toObject(raw: ArrayList<RepoEntity>?): ArrayList<RepoEntity>? {
                return raw
            }
        }
    }

    //======================================================================
    // Parameter
    //======================================================================

    open class Parameter {
        var org: String? = ""
        var repo: String? = ""
    }
}