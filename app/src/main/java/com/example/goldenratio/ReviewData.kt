package com.example.goldenratio

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//리뷰 데이터
data class ReviewData(@SerializedName("reviewer") var reviewer: String,
                      @SerializedName("rating") var rating: Float,
                      @SerializedName("comment") var comment: String,
                      @SerializedName("profileImageUrl") var profileImageUrl: String,)