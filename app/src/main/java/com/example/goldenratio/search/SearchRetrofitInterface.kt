package com.example.goldenratio.search

import retrofit2.Call
import retrofit2.http.*

interface SearchRetrofitInterface {
    //재료 검색
    @GET("/golden-ratio/search")
    fun getSearch(
        @Header("Authorization") accessToken: String,
        @Query("name") name: String
    ):Call<SearchResponse>
}
