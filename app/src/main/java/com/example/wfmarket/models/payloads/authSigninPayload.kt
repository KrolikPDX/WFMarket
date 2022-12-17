package com.example.wfmarket.models.payloads

data class AuthSigninPayload (val email:String, val password:String, val auth_type:String = "header")