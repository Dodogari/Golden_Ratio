package com.example.goldenratio

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RegisterClient {
    //사용하고 있는 서버 BASE 주소 => 변경 필요
    private var baseUrl = "https://d4f37515-8b75-4487-861a-5693493e64ec.mock.pstmn.io"

    //retrofit 설정
    private var retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val registerService: CocktailInterface by lazy { retrofit.create(CocktailInterface::class.java) }
}