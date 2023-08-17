package com.example.goldenratio.hangover.models

import com.google.gson.annotations.SerializedName
import java.net.URL

data class PostHangRequest(
        @SerializedName("title") val title: String,
        @SerializedName("hangoverMainImageUrl") val hangoverMainImageUrl: URL?,
        @SerializedName("content") val content: String,
        @SerializedName("category") val category: String,
        @SerializedName("gradientList") val gradientList: ArrayList<HangRequest>
)