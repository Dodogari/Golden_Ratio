package com.example.goldenratio

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
//리뷰 데이터
data class ReviewData(var userName: String,
                      var userProfile: Int,
                      var rating: Int,
                      var content: String): Parcelable