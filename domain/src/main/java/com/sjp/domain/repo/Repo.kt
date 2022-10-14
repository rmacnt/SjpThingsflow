package com.sjp.domain.repo

import com.google.gson.annotations.SerializedName

data class Repo(
    @SerializedName("url")
    var url: String?,
    @SerializedName("repository_url")
    var repository_url: String?,
    @SerializedName("comments_url")
    var comments_url: String?,
    @SerializedName("events_url")
    var events_url: String?,
    @SerializedName("html_url")
    var html_url: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("node_id")
    var node_id: String?,
    @SerializedName("number")
    var number: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("user")
    var user: RepoUser?,
    @SerializedName("labels")
    var labels: ArrayList<RepoLabel?>,
    @SerializedName("state")
    var state: String?,
    @SerializedName("locked")
    var locked: Boolean?,
    @SerializedName("assignee")
    var assignee: RepoAssignees?,
    @SerializedName("assignees")
    var assignees: ArrayList<RepoAssignees>?,
    @SerializedName("milestone")
    var milestone: String?,
    @SerializedName("comments")
    var comments: Int?,
    @SerializedName("created_at")
    var created_at: String?,
    @SerializedName("updated_at")
    var updated_at: String?,
    @SerializedName("closed_at")
    var closed_at: String?,
    @SerializedName("author_association")
    var author_association: String?,
    @SerializedName("active_lock_reason")
    var active_lock_reason: String?,
    @SerializedName("body")
    var body: String?,
    @SerializedName("reactions")
    var reactions: RepoReactions?,
    @SerializedName("timeline_url")
    var timeline_url: String?,
    @SerializedName("performed_via_github_app")
    var performed_via_github_app: String?,
    @SerializedName("state_reason")
    var state_reason: String?

)
