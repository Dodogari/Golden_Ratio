package com.example.goldenratio.login.models.register

import android.util.Log
import com.example.config.ApplicationClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterService(val RegisterInterface: RegisterInterface) {
    fun tryPostRegister(PostRegisterRequest: PostRegisterRequest){
        val registerRetrofitInterface = ApplicationClass.sRetrofit?.create(RegisterRetrofitInterface::class.java)
        registerRetrofitInterface?.postRegister(PostRegisterRequest)?.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                (response.body() as RegisterResponse?)?.let {
                    RegisterInterface.onPostRegisterSuccess(
                        it
                    )
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                RegisterInterface.onPostRegisterFailure(t.message ?: "통신 오류")
                Log.d("error","연결 실패")
            }
        })
    }
}
