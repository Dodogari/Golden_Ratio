package com.example.goldenratio

import com.example.GradientData
import com.google.gson.annotations.SerializedName

//칵테일 데이터 세부
data class CocktailData(@SerializedName("id") var id: Int,
                        @SerializedName("title") var title: String,
                        @SerializedName("content") val content: String,
                        @SerializedName("mainImage") var mainImage: Int,
                        @SerializedName("category") var category: String,
                        @SerializedName("averageScore") var averageScore: Float,
                        @SerializedName("sweet") var sweet: Int,
                        @SerializedName("alcohol") var alcohol: Int,
                        @SerializedName("likes") var likes: Int,
                        @SerializedName("gradientList") var gradientList: ArrayList<GradientData>,
                        @SerializedName("reviews") var reviews: ArrayList<ReviewData>,
                        @SerializedName("balanceList") var balanceList: ArrayList<BalanceData>,
                        @SerializedName("createdDate") var createdDate: ArrayList<Int>,
                        @SerializedName("lastModifiedTime") var lastModifiedTime: ArrayList<Int>)