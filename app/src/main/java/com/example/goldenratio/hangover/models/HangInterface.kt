package com.example.goldenratio.hangover.models

interface HangInterface {
    fun onPostHangSuccess(response: HangResponse)
    fun onPostHangFailure(message: String)
}