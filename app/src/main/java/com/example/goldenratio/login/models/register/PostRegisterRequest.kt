package com.example.goldenratio.login.models.register

import com.google.gson.annotations.SerializedName
import java.net.URL

data class PostRegisterRequest(
        @SerializedName("userId") val id: String,
        @SerializedName("password") val password: String,
        @SerializedName("nickname") val nickname: String,
        @SerializedName("profileImageUrl") val url: URL?
)