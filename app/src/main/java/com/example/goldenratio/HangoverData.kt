package com.example.goldenratio

import com.example.GradientData

data class HangoverData(var title: String,
                        var thumbnail: Int,
                        var rating: Float,
                        var like: Int,
                        var likeCheck: Boolean,
                        var writtenDate: String,
                        var gradientList: ArrayList<GradientData>,
                        var recipeContent: String,
                        var reviewList: ArrayList<ReviewData>)
