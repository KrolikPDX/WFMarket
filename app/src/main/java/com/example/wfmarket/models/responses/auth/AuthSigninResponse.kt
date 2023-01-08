package com.example.wfmarket.models.responses.auth

data class AuthSigninResponse (
    val payload: Payload
)

data class Payload (
    val user: User
)

data class User(
    val verification: Boolean,
    val linkedAccounts: LinkedAccounts?,
    val role: String,
    val anonymous: Boolean,
    val id: String,
    val platform: String,
    val avatar: Any? = null,
    val unreadMessages: Long,
    val writtenReviews: Long,
    val checkCode: String,
    val region: String,
    val banned: Boolean,
    val reputation: Long,
    val background: Any? = null,
    val hasMail: Boolean
)

data class LinkedAccounts (
    val steamProfile: Boolean,
    val patreonProfile: Boolean,
    val xboxProfile: Boolean,
    val discordProfile: Boolean,
    val githubProfile: Boolean
)
