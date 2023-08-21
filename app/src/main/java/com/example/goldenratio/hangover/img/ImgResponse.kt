package com.example.goldenratio.hangover.img

import com.google.gson.annotations.SerializedName
import java.net.URL

data class ImgResponse(
        @SerializedName("result") val result : URL?
)


