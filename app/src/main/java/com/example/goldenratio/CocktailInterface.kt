package com.example.goldenratio

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CocktailInterface {
    //칵테일 조회 - 전체
    @GET("/golden-ratio/cocktail/all")
    fun getCocktailAll():Call<ArrayList<BoardData>>

    //칵테일 조회 - 별점순
    @GET("/golden-ratio/cocktail/star")
    fun getCocktailStar():Call<ArrayList<BoardData>>

    //칵테일 조회 - 좋아요순
    @GET("/golden-ratio/cocktail/like")
    fun getCocktailLike():Call<ArrayList<BoardData>>

    //칵테일 조회 - 도수높은순
    @GET("/golden-ratio/cocktail/alchol")
    fun getCocktailAlcohol():Call<ArrayList<BoardData>>

    //칵테일 조회 - 단맛높은순
    @GET("/golden-ratio/cocktail/sweet")
    fun getCocktailSweet():Call<ArrayList<BoardData>>

    //숙취해소 전체 조회
    @GET("/golden-ratio/hangover/all")
    fun getHangoverListAll():Call<ArrayList<BoardData>>

    //숙취해소 조회 - 별점순
    @GET("/golden-ratio/hangover/star")
    fun getHangoverListStar():Call<ArrayList<BoardData>>

    //숙취해소 조회 - 좋아요순
    @GET("/golden-ratio/hangover/like")
    fun getHangoverListLike():Call<ArrayList<BoardData>>

    //게시글 상세 조회 - 칵테일
    @GET("/golden-ratio/cocktail/{board-id}")
    fun getCocktailItem(@Query("board-id") boardId: String):Call<CocktailData>
    
    //게시글 상세 조회 - 숙취해소
    @GET("/golden-ratio/hangover/{board-id}")
    fun getHangoverItem(@Query("board-id") boardId: String):Call<CocktailData>
}