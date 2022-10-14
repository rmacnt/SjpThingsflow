package com.sjp.data.api.entity.repo

import com.google.gson.annotations.SerializedName

data class RepoAssigneesEntity (

    @SerializedName("login")
    var login: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("node_id")
    var node_id: String?,
    @SerializedName("avatar_url")
    var avatar_url: String?,
    @SerializedName("gravatar_id")
    var gravatar_id: String?,
    @SerializedName("url")
    var url: String?,
    @SerializedName("html_url")
    var html_url: String?,
    @SerializedName("followers_url")
    var followers_url: String?,
    @SerializedName("following_url")
    var following_url: String?,
    @SerializedName("gists_url")
    var gists_url: String?,
    @SerializedName("starred_url")
    var starred_url: String?,
    @SerializedName("subscriptions_url")
    var subscriptions_url: String?,
    @SerializedName("organizations_url")
    var organizations_url: String?,
    @SerializedName("events_url")
    var events_url: String?,
    @SerializedName("repos_url")
    var repos_url: String?,
    @SerializedName("received_events_url")
    var received_events_url: String?,
    @SerializedName("type")
    var type: String?,
    @SerializedName("site_admin")
    var site_admin: Boolean?

)