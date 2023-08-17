package com.example

import com.google.gson.annotations.SerializedName

data class GradientData(@SerializedName("gradientName") var gradientName: String,
                        @SerializedName("gradientImageUrl") var gradientImageUrl: String)
