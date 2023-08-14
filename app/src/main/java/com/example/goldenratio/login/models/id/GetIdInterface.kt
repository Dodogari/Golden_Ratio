package com.example.goldenratio.login.models.id

interface GetIdInterface {
    // 이메일 인증번호 확인
    fun onGetIdSuccess(response: IdCheckResponse)
    fun onGetIdFailure(message: String)
}