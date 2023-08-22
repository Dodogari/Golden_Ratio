package com.example.goldenratio.search

interface SearchInterface {
    // 이메일 인증번호 확인
    fun onSearchSuccess(response: SearchResponse)
    fun onSearchFailure(message: String)
}