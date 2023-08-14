package com.example.goldenratio

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BalanceData(var balanceName: String,
                       var balanceNum: Int): Parcelable
