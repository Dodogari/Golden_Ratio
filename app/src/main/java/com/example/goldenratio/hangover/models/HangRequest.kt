package com.example.goldenratio.hangover.models

import com.google.gson.annotations.SerializedName
import retrofit2.http.Url

data class HangRequest(
        @SerializedName("gradientName") val Name: String,
        @SerializedName("gradientImageUrl") val url: Url
)