package com.example.goldenratio

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewInterface {
    //리뷰 전체 조회
    @GET("/golden-ratio/review/{board-id}")
    fun getReviewAll(@Path("board-id") boardId: String): Call<ArrayList<ReviewItemData>>

    //리뷰 등록
    @POST("/golden-ratio/review/{board-id}")
    fun registerReview(@Path("board-id") boardId: String, @Body requestData: ReviewRegisterData): Call<PostResponse>
}