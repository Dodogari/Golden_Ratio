package com.example.goldenratio.login.models

import com.google.gson.annotations.SerializedName

data class PostLoginRequest(
        @SerializedName("userId") val id: String,
        @SerializedName("password") val password: String
)