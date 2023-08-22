package com.example.goldenratio.cocktail.models

import retrofit2.Call
import retrofit2.http.*

interface CocktailRetrofitInterface {
    @POST("/golden-ratio/cocktail")
    fun postCocktail(
        @Header("Authorization") accessToken: String,
        @Body params: PostCocktailRequest
    ): Call<CocktailResponse>

    @POST("/golden-ratio/cocktail/{board-id}")
    fun editCocktail(
        @Path("board-id") boardId: String,
        @Header("Authorization") accessToken: String,
        @Body params: PostCocktailRequest
    ): Call<CocktailResponse>
}
