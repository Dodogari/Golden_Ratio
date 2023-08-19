package com.example.goldenratio

import com.google.gson.annotations.SerializedName

data class ReviewRegisterData(@SerializedName("content") var content: String,
                              @SerializedName("score") var score: Float)
