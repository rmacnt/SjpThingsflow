package com.sjp.data.api.entity.repo

import com.google.gson.annotations.SerializedName

data class RepoLabelEntity(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("node_id")
    var node_id: String?,
    @SerializedName("url")
    var url: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("color")
    var color: String?,
    @SerializedName("default")
    var default: Boolean?,
    @SerializedName("description")
    var description: String?
)
