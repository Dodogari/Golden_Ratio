package com.example.goldenratio.hangover.img

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ImgRetrofitInterface {

    // 이미지 URL API
    @Multipart
    @POST("/golden-ratio/url")
    fun postJoin(
        @Header("token") token: String?,
        @Part image : MultipartBody.Part?
    ): Call<ImgResponse>
}
