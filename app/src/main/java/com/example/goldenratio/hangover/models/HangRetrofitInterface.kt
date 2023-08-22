package com.example.goldenratio.hangover.models

import retrofit2.Call
import retrofit2.http.*

interface HangRetrofitInterface {
    @POST("/golden-ratio/hangover")
    fun postHang(
        @Header("Authorization") accessToken: String,
        @Body params: PostHangRequest
    ): Call<HangResponse>

    @POST("/golden-ratio/hangover/{board-id}")
    fun editHang(
        @Path("board-id") boardId: String,
        @Header("Authorization") accessToken: String,
        @Body params: PostHangRequest
    ): Call<HangResponse>
}
