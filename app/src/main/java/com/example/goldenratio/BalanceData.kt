package com.example.goldenratio

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class BalanceData(var balanceName: String,
                       var balanceNum: Int): Parcelable
