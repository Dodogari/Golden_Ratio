package com.example.goldenratio.login.models.register

interface RegisterInterface {
    fun onPostRegisterSuccess(response: RegisterResponse)
    fun onPostRegisterFailure(message: String)
}