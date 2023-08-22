package com.example.goldenratio.img

interface ImgInterface {
    fun onPostImgSuccess(response: ImgResponse)
    fun onPostImgFailure(message: String)
}