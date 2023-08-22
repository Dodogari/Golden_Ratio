package com.example.goldenratio

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RegisterClient {
    //사용하고 있는 서버 BASE 주소 => 변경 필요
    //https://golden-ratio.store
    private var baseUrl = "https://golden-ratio.store"

    //retrofit 설정
    private var retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val cocktailService: CocktailInterface by lazy { retrofit.create(CocktailInterface::class.java) }
    val hangoverService: HangoverInterface by lazy { retrofit.create(HangoverInterface::class.java) }
    val reviewService: ReviewInterface by lazy { retrofit.create(ReviewInterface::class.java) }
}