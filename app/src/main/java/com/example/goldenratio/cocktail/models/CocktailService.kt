package com.example.goldenratio.cocktail.models

import android.util.Log
import com.example.config.ApplicationClass
import com.example.goldenratio.login.accessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CocktailService(val CocktailInterface: CocktailInterface) {
    fun tryPostCocktail(PostCocktailRequest: PostCocktailRequest){
        val CocktailRetrofitInterface = ApplicationClass.sRetrofit?.create(CocktailRetrofitInterface::class.java)
        CocktailRetrofitInterface?.postCocktail("Bearer " + accessToken, PostCocktailRequest)?.enqueue(object : Callback<CocktailResponse>{
            override fun onResponse(call: Call<CocktailResponse>, response: Response<CocktailResponse>) {
                (response.body() as CocktailResponse?)?.let {
                    CocktailInterface.onPostCocktailSuccess(
                        it
                    )
                }
            }

            override fun onFailure(call: Call<CocktailResponse>, t: Throwable) {
                CocktailInterface.onPostCocktailFailure(t.message ?: "통신 오류")
                Log.d("error","연결 실패")
            }
        })
    }
}
