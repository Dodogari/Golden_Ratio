package com.example.goldenratio.login.models

import retrofit2.Call
import retrofit2.http.*

interface LoginRetrofitInterface {
    @POST("/login")
    fun postLogin(@Body params: PostLoginRequest): Call<LoginResponse>
}
