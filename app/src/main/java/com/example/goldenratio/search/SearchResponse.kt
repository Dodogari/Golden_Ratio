package com.example.goldenratio.search
import com.example.config.BaseResponse
import com.google.gson.annotations.SerializedName
import java.net.URL

data class SearchResponse(
        @SerializedName("gradientName") val gradientName : String,
        @SerializedName("gradientImageUrl") val gradientImageUrl : URL
):BaseResponse()