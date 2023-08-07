package com.example.goldenratio.login.models
import com.google.gson.annotations.SerializedName

data class ResultLogin(
        @SerializedName("accessToken") val accessToken : String,
        @SerializedName("refreshToken") val refreshToken : String
)