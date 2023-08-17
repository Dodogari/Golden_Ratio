package com.example.goldenratio

import android.os.Parcelable
import com.example.GradientData
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//재료 데이터
data class RecipeData (@SerializedName("gradientList") var gradientList: ArrayList<GradientData>,
                       @SerializedName("balanceList") var balanceList: ArrayList<BalanceData>)