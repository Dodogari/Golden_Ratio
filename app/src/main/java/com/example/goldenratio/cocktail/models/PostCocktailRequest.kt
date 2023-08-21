package com.example.goldenratio.cocktail.models

import com.google.gson.annotations.SerializedName
import java.net.URL

data class PostCocktailRequest(
        @SerializedName("title") val title: String,
        @SerializedName("cocktailMainImageUrl") val cocktailMainImageUrl: URL?,
        @SerializedName("content") val content: String,
        @SerializedName("category") val category: String,
        @SerializedName("sweet") val sweet: Int?,
        @SerializedName("alcohol") val alcohol: Int?,
        @SerializedName("gradientList") val gradientList: ArrayList<CocktailRequest>,
        @SerializedName("balanceList") val balanceList: ArrayList<BalanceListRequest>
)