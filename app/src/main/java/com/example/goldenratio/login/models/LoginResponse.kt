package com.example.goldenratio.login.models
import com.example.config.BaseResponse
import com.google.gson.annotations.SerializedName

data class LoginResponse(
        @SerializedName("result") val result: ResultLogin
): BaseResponse() // 베이스 리스폰스를 상속 받음


