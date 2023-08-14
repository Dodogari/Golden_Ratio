package com.example.goldenratio.hangover.models

import retrofit2.Call
import retrofit2.http.*

interface HangRetrofitInterface {
    @POST("/golden-ratio/hangover")
    fun postRegister(@Body params: PostHangRequest): Call<HangResponse>
}
