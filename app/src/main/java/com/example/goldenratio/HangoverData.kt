package com.example.goldenratio

data class HangoverData(var title: String,
                        var thumbnail: Int,
                        var rating: Float,
                        var like: Int,
                        var likeCheck: Boolean,
                        var writtenDate: String,
                        var gradientList: ArrayList<RecipeData>,
                        var recipeContent: String,
                        var reviewList: ArrayList<ReviewData>)
