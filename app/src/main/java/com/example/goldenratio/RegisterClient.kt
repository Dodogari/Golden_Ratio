package com.example.goldenratio

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RegisterClient {
    //사용하고 있는 서버 BASE 주소 => 변경 필요
    private var baseUrl = "https://a34e0232-abb8-403a-9840-d79ef686a4d5.mock.pstmn.io"

    //retrofit 설정
    private var retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val cocktailService: CocktailInterface by lazy { retrofit.create(CocktailInterface::class.java) }
    val hangoverService: HangoverInterface by lazy { retrofit.create(HangoverInterface::class.java) }
    val reviewService: ReviewInterface by lazy { retrofit.create(ReviewInterface::class.java) }
}