package com.example.goldenratio.login.models.register
import com.example.config.BaseResponse
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
        @SerializedName("result") val result : String
):BaseResponse()


