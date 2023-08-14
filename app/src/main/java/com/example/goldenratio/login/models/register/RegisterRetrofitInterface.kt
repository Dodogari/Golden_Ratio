package com.example.goldenratio.login.models.register

import retrofit2.Call
import retrofit2.http.*

interface RegisterRetrofitInterface {
    @POST("/join")
    fun postRegister(@Body params: PostRegisterRequest): Call<RegisterResponse>
}
