package com.example.goldenratio

import retrofit2.Call
import retrofit2.http.*

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

    //게시글 상세 조회 - 칵테일
    @GET("/golden-ratio/cocktail/{board-id}")
    fun getCocktailItem(@Path("board-id") boardId: String):Call<CocktailData>

    //좋아요 등록
    @POST("/golden-ratio/like/{board-id}")
    fun registerLikes (@Path("board-id") boardId: String, @Header("Authorization") accessToken: String): Call<PostResponse>
}