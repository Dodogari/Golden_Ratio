package com.example.config

import com.example.goldenratio.login.accessToken
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class XAccessTokenInterceptor : Interceptor {
    @Throws(IOException::class)

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        //val jwtToken: String? = sSharedPreferences.getString(X_ACCESS_TOKEN, null)
        if (accessToken != null) {
            builder.addHeader("Authorization", "Bearer " + accessToken.toString())
        }
        return chain.proceed(builder.build())
    }
}