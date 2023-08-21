package com.example.goldenratio.cocktail.models

import com.google.gson.annotations.SerializedName

data class BalanceListRequest(
        @SerializedName("balanceName") val balanceName: String,
        @SerializedName("balanceNum") val balanceNum: Int
)