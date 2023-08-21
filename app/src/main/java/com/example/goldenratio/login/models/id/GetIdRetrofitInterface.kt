package com.example.goldenratio.login.models.id

import retrofit2.Call
import retrofit2.http.*

interface GetIdRetrofitInterface {
    //아이디 중복확인
    @GET("/check/{user-id}")
    fun getID(@Path("user-id") userId: String):Call<IdCheckResponse>
}
