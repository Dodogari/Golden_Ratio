package com.example.goldenratio.hangover.models

import com.google.gson.annotations.SerializedName
import retrofit2.http.Url

data class PostHangRequest(
        @SerializedName("title") val title: String,
        @SerializedName("hangoverMainImageUrl") val hangoverMainImageUrl: Url,
        @SerializedName("content") val content: String,
        @SerializedName("category") val category: String,
        @SerializedName("gradientList") val gradientList: ArrayList<HangRequest>
)