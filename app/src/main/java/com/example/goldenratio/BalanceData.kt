package com.example.goldenratio

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class BalanceData(@SerializedName("balanceName") var balanceName: String,
                       @SerializedName("balanceNum") var balanceNum: Int)
