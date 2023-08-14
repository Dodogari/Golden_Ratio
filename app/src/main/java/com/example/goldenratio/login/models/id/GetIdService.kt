package com.example.goldenratio.login.models.id

import com.example.config.ApplicationClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetIdService(val GetIdInterface: GetIdInterface) {

    fun tryGetId(userId: String){
        val GetIdRetrofitInterface = ApplicationClass.sRetrofit?.create(GetIdRetrofitInterface::class.java)
        GetIdRetrofitInterface?.getID(userId)?.enqueue(object : Callback<IdCheckResponse>{
            override fun onResponse(call: Call<IdCheckResponse>, response: Response<IdCheckResponse>) {
                (response.body() as IdCheckResponse?)?.let {
                    GetIdInterface.onGetIdSuccess(
                        it
                    )
                }
            }

            override fun onFailure(call: Call<IdCheckResponse>, t: Throwable) {
                GetIdInterface.onGetIdFailure(t.message ?: "통신 오류")
            }
        })
    }
}
