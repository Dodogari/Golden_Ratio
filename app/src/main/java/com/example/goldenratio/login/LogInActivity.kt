package com.example.goldenratio.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.goldenratio.MainActivity
import com.example.goldenratio.databinding.ActivityLoginBinding
import com.example.goldenratio.login.models.*

var accessToken : String? = null
var userId: String = ""

class LoginActivity : AppCompatActivity(), LoginInterface {
    private lateinit var loginBinding: ActivityLoginBinding
    private var tv_id: String = ""
    private var tv_pw: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        // 메인 화면으로 이동
        loginBinding.btLogin.setOnClickListener {
            // 서버에 값 보냄
            val id = loginBinding.etId.text.toString()
            val password = loginBinding.etPw.text.toString()
            val postRequest = PostLoginRequest(id = id, password = password)

            LoginService(this).tryPostLogin(postRequest)
        }

        //버튼 비활성화
        loginBinding.btLogin.isEnabled = false

        // id / pw 입력 시 버튼 활성화
        loginBinding.etId.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tv_id = loginBinding.etId.text.toString()
                tv_pw = loginBinding.etPw.text.toString()

                loginBinding.btLogin.isEnabled = tv_id.isNotEmpty() && tv_pw.isNotEmpty()

                userId = tv_id
            }
        })

        loginBinding.etPw.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tv_id = loginBinding.etId.text.toString()
                tv_pw = loginBinding.etPw.text.toString()

                loginBinding.btLogin.isEnabled = tv_id.isNotEmpty() && tv_pw.isNotEmpty()
            }
        })

        // 회원가입 화면으로 이동
        loginBinding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }

    // 서버 연결 성공
    override fun onPostLoginSuccess(response: LoginResponse) {
        // 메인 화면으로 이동
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)

        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
        accessToken = response.accessToken
    }

    // 서버 연결 실패
    override fun onPostLoginFailure(message: String) {
        Log.d("error", "오류 : $message")
        Toast.makeText(this, "오류 : ${message}", Toast.LENGTH_SHORT).show()
    }
}

