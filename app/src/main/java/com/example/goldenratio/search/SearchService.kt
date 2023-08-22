package com.example.goldenratio.search

import android.util.Log
import com.example.config.ApplicationClass
import com.example.goldenratio.login.accessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchService(val SearchInterface: SearchInterface) {
    fun tryGetSearch(name: String){
        val SearchRetrofitInterface = ApplicationClass.sRetrofit?.create(SearchRetrofitInterface::class.java)
        SearchRetrofitInterface?.getSearch("Bearer $accessToken", name)?.enqueue(object : Callback<SearchResponse>{
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                (response.body() as SearchResponse?)?.let {
                    SearchInterface.onSearchSuccess(
                        it
                    )
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                SearchInterface.onSearchFailure(t.message ?: "통신 오류")
                Log.d("tag", "통신 오류")
            }
        })
    }
}
