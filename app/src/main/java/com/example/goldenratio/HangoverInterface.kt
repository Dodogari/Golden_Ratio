package com.example.goldenratio

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface HangoverInterface {
    //숙취해소 전체 조회
    @GET("/golden-ratio/hangover/all")
    fun getHangoverListAll():Call<ArrayList<BoardData>>

    //숙취해소 조회 - 별점순
    @GET("/golden-ratio/hangover/star")
    fun getHangoverListStar():Call<ArrayList<BoardData>>

    //숙취해소 조회 - 좋아요순
    @GET("/golden-ratio/hangover/like")
    fun getHangoverListLike():Call<ArrayList<BoardData>>

    //게시글 상세 조회 - 숙취해소
    @GET("/golden-ratio/hangover/{board-id}")
    fun getHangoverItem(@Path("board-id") boardId: String): Call<HangoverData>
}