package com.example.goldenratio

import com.google.gson.annotations.SerializedName

//제목, 썸네일, 좋아요 갯수, 좋아요 체크 여부, 날짜, 알콜 도수, 단맛, 레시피, 재료 리스트(이름, 이미지), 레시피 설명, 비율 리스트(이름, 비율)
data class CocktailData(@SerializedName("id") var id: Int,
                        @SerializedName("title") var title: String,
                        @SerializedName("content") val content: String,
                        @SerializedName("mainImage") var mainImage: Int,
                        @SerializedName("category") var cocktail: String,
                        @SerializedName("averageScore") var averageScore: Float,
                        @SerializedName("alcohol") var alcohol: Int,
                        @SerializedName("sweet") var sweetLevel: String,
                        @SerializedName("likes") var likes: Int,
                        @SerializedName("recipe") var recipeList: ArrayList<RecipeData>,
                        @SerializedName("reviews") var reviews: ArrayList<ReviewData>)