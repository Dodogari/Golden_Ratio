package com.example.goldenratio

import com.google.gson.annotations.SerializedName

data class BoardData(@SerializedName("title") var title: String,
                     @SerializedName("mainImage") var mainImageUrl: String,
                     @SerializedName("averageScore") var averageScore: Float,
                     @SerializedName("likeCount") var likeCount: Int,
                     @SerializedName("boardId") var boardId: Int)