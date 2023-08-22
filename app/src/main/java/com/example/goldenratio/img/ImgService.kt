package com.example.goldenratio.img

import android.util.Log
import com.example.config.ApplicationClass
import com.example.goldenratio.login.accessToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ImgService(val ImgInterface: ImgInterface) {
    fun tryPostImg(file: File){
        val ImgRetrofitInterface = ApplicationClass.sRetrofit?.create(ImgRetrofitInterface::class.java)
        val emptyImage = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val image = MultipartBody.Part.createFormData("image", file.name, emptyImage)
        ImgRetrofitInterface?.postImg("Bearer $accessToken", image)?.enqueue(object : Callback<ImgResponse>{
            override fun onResponse(call: Call<ImgResponse>, response: Response<ImgResponse>) {
                (response.body() as ImgResponse?)?.let {
                    ImgInterface.onPostImgSuccess(
                        it
                    )
                }
            }

            override fun onFailure(call: Call<ImgResponse>, t: Throwable) {
                ImgInterface.onPostImgFailure(t.message ?: "통신 오류")
                Log.d("error","연결 실패")
            }
        })
    }
}
