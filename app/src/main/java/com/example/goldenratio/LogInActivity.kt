package com.example.goldenratio

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.goldenratio.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    private var id: String = ""
    private var pw: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        // 메인 화면으로 이동
        loginBinding.btLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

        //버튼 비활성화
        loginBinding.btLogin.isEnabled = false

        // id / pw 입력 시 버튼 활성화
        loginBinding.inputId.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                id = loginBinding.inputId.text.toString()
                pw = loginBinding.inputPw.text.toString()

                loginBinding.btLogin.isEnabled = id.isNotEmpty() && pw.isNotEmpty()
            }
        })

        loginBinding.inputPw.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                id = loginBinding.inputId.text.toString()
                pw = loginBinding.inputPw.text.toString()

                loginBinding.btLogin.isEnabled = id.isNotEmpty() && pw.isNotEmpty()
            }
        })

        // 회원가입 화면으로 이동
        loginBinding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            this.startActivity(intent)
        }
    }
}

