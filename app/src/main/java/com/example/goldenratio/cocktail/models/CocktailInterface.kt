package com.example.goldenratio.cocktail.models

interface CocktailInterface {
    fun onPostCocktailSuccess(response: CocktailResponse)
    fun onPostCocktailFailure(message: String)
}