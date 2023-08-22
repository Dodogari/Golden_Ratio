package com.example.goldenratio.img

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ImgRetrofitInterface {
    // 이미지 URL API
    @Multipart
    @POST("/golden-ratio/url")
    fun postImg(
        @Header("Authorization") accessToken: String?,
        @Part image : MultipartBody.Part?
    ): Call<ImgResponse>
}
