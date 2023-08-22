package com.example.goldenratio

import com.google.gson.annotations.SerializedName

//ReviewActivity - 리뷰 아이템 데이터
data class ReviewItemData(@SerializedName("id") var id : Int,
                          @SerializedName("boardId") var boardId : Int,
                          @SerializedName("userId") var reviewer: Int,
                          @SerializedName("content") var comment: String,
                          @SerializedName("profileImageUrl") var profileImageUrl: String,
                          @SerializedName("score") var rating: Float)