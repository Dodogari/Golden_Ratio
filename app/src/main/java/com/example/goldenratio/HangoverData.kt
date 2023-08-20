package com.example.goldenratio

import com.example.GradientData
import com.google.gson.annotations.SerializedName

data class HangoverData(@SerializedName("id") var id: Int,
                        @SerializedName("title") var title: String,
                        @SerializedName("content") val content: String,
                        @SerializedName("mainImage") var mainImage: String,
                        @SerializedName("category") var category: String,
                        @SerializedName("averageScore") var averageScore: Float,
                        @SerializedName("likes") var likes: Int,
                        @SerializedName("gradientList") var gradientList: ArrayList<GradientData>,
                        @SerializedName("reviews") var reviews: ArrayList<ReviewData>,
                        @SerializedName("createdDate") var createdDate: ArrayList<Int>,
                        @SerializedName("lastModifiedTime") var lastModifiedTime: ArrayList<Int>)
