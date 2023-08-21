package com.example.goldenratio.cocktail.models

import com.google.gson.annotations.SerializedName
import java.net.URL

data class CocktailRequest(
        @SerializedName("gradientName") val name: String,
        @SerializedName("gradientImageUrl") val url: URL?
)