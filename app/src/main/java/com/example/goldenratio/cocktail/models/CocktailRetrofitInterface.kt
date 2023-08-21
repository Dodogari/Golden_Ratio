package com.example.goldenratio.cocktail.models

import retrofit2.Call
import retrofit2.http.*

interface CocktailRetrofitInterface {
    @POST("/golden-ratio/cocktail")
    fun postCocktail(@Body params: PostCocktailRequest): Call<CocktailResponse>
}
