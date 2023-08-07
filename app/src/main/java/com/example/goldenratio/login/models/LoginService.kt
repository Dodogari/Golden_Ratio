package com.example.goldenratio.login.models

import com.example.config.ApplicationClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginService(val LoginInterface: LoginInterface) {

    fun tryPostLogin(PostLoginRequest: PostLoginRequest){
        val loginRetrofitInterface = ApplicationClass.sRetrofit.create(LoginRetrofitInterface::class.java)
        loginRetrofitInterface.postLogin(PostLoginRequest).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                (response.body() as LoginResponse?)?.let {
                    LoginInterface.onPostLoginSuccess(
                        it
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                LoginInterface.onPostLoginFailure(t.message ?: "통신 오류")
            }
        })
    }

}
