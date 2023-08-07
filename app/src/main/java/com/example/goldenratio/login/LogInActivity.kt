package com.example.goldenratio.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.goldenratio.MainActivity
import com.example.goldenratio.databinding.ActivityLoginBinding
import com.example.goldenratio.hangover.NewHangoverActivity
import com.example.goldenratio.login.models.LoginInterface
import com.example.goldenratio.login.models.LoginResponse
import com.example.goldenratio.login.models.LoginService
import com.example.goldenratio.login.models.PostLoginRequest


class LoginActivity : AppCompatActivity(), LoginInterface {
    private lateinit var loginBinding: ActivityLoginBinding
    private var id: String = ""
    private var pw: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        // 메인 화면으로 이동
        loginBinding.btLogin.setOnClickListener {
            val intent = Intent(this, NewHangoverActivity::class.java)
            this.startActivity(intent)

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
                id = loginBinding.etId.text.toString()
                pw = loginBinding.etPw.text.toString()

                loginBinding.btLogin.isEnabled = id.isNotEmpty() && pw.isNotEmpty()
            }
        })

        loginBinding.etPw.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                id = loginBinding.etId.text.toString()
                pw = loginBinding.etPw.text.toString()

                loginBinding.btLogin.isEnabled = id.isNotEmpty() && pw.isNotEmpty()
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
        // 계정이 있는 경우
        if(response.isSuccess){
            // 메인 화면으로 이동
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }
        // Result message
        response.message?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
    }

    // 서버 연결 실패
    override fun onPostLoginFailure(message: String) {
        Toast.makeText(this, "오류 : $message", Toast.LENGTH_SHORT).show()
    }
}

