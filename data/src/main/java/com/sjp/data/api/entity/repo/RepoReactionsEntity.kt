package com.sjp.data.api.entity.repo

import com.google.gson.annotations.SerializedName

data class RepoReactionsEntity(
    @SerializedName("login")
    var login: String?,
    @SerializedName("total_count")
    var total_count: Int?,
    @SerializedName("laugh")
    var laugh: Int?,
    @SerializedName("hooray")
    var hooray: Int?,
    @SerializedName("confused")
    var confused: Int?,
    @SerializedName("heart")
    var heart: Int?,
    @SerializedName("rocket")
    var rocket: Int?,
    @SerializedName("eyes")
    var eyes: Int?

)