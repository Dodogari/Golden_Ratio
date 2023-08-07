package com.example.goldenratio.login.models

interface LoginInterface {
    fun onPostLoginSuccess(response: LoginResponse)
    fun onPostLoginFailure(message: String)
}