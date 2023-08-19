package com.example.goldenratio

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ReviewInterface {
    //숙취해소 조회 - 좋아요순
    @GET("/golden-ratio/review/{board-id}")
    fun getReviewAll(@Path("board-id") boardId: String): Call<ArrayList<ReviewItemData>>
}