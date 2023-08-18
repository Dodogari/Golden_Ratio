package com.example.goldenratio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.goldenratio.databinding.ActivityAgreementBinding

class AgreementActivity : AppCompatActivity() {
    private lateinit var agreementBinding: ActivityAgreementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        agreementBinding = ActivityAgreementBinding.inflate(layoutInflater)
        setContentView(agreementBinding.root)

        //뒤로 가기 버튼: 현재 약관 액티비티 종료
        agreementBinding.buttonBackAgreement.setOnClickListener {
            finish()
        }
    }
}