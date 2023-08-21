package com.example.goldenratio.hangover.models

import android.util.Log
import com.example.config.ApplicationClass
import com.example.goldenratio.login.accessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HangService(val HangInterface: HangInterface) {
    fun tryPostRegister(PostHangRequest: PostHangRequest){
        val HangRetrofitInterface = ApplicationClass.sRetrofit?.create(HangRetrofitInterface::class.java)
        HangRetrofitInterface?.postHang("Bearer " + accessToken, PostHangRequest)?.enqueue(object : Callback<HangResponse>{
            override fun onResponse(call: Call<HangResponse>, response: Response<HangResponse>) {
                (response.body() as HangResponse?)?.let {
                    HangInterface.onPostHangSuccess(
                        it
                    )
                }
            }

            override fun onFailure(call: Call<HangResponse>, t: Throwable) {
                HangInterface.onPostHangFailure(t.message ?: "통신 오류")
                Log.d("error","연결 실패")
            }
        })
    }
}
