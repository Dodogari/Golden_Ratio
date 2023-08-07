package com.example.goldenratio

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//재료 데이터
data class RecipeData (@SerializedName("gradientName") var gradientName: String,
                       @SerializedName("gradientImageUrl") var gradientImageUrl: String,
                       @SerializedName("gradientRatio") var gradientRatio: Int)