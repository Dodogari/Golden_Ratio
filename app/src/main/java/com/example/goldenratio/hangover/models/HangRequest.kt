package com.example.goldenratio.hangover.models

import com.google.gson.annotations.SerializedName
import java.net.URL

data class HangRequest(
        @SerializedName("gradientName") val Name: String,
        @SerializedName("gradientImageUrl") val url: URL?
)