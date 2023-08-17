package com.example.goldenratio.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.goldenratio.cocktail.IngredientFragment
import com.example.goldenratio.databinding.ActivityRegisterBinding
import com.example.goldenratio.login.models.id.GetIdInterface
import com.example.goldenratio.login.models.id.GetIdService
import com.example.goldenratio.login.models.id.IdCheckResponse

var id: String = ""
var pw: String = ""

class RegisterActivity : AppCompatActivity(), GetIdInterface {
    private lateinit var registerBinding: ActivityRegisterBinding

    companion object{
        lateinit var RegisterContext : Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = com.example.goldenratio.databinding.ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        RegisterContext = this

        //버튼 비활성화
        registerBinding.btIdCheck.isEnabled = true
        registerBinding.btNext.isEnabled = false

        // id / pw 입력 시 버튼 활성화
        registerBinding.etPw2.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            // 입력 중
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                id = registerBinding.etId.text.toString()
                pw = registerBinding.etPw2.text.toString()

                registerBinding.btNext.isEnabled = id.isNotEmpty() && pw.isNotEmpty()
            }
        })

        // 아이디 중복 확인
        registerBinding.btIdCheck.setOnClickListener {
            // id 확인
            if (!validateEmail()) {
                return@setOnClickListener
            }

            if (validateEmail()) {
                registerBinding.btIdCheck.isEnabled = false
            }

            // 서버 연결
            if(id != null)
            {
                id = registerBinding.etId.text.toString()
                GetIdService(this).tryGetId(userId = id)
            }
            registerBinding.btIdCheck.isEnabled = false
        }

        registerBinding.btNext.setOnClickListener{
            // 비밀번호 확인
            if (!checkPw()) {
                return@setOnClickListener
            }

            // 이용약관 확인
            if(!registerBinding.btAgree.isChecked)
            {
                Toast.makeText(this, "이용약관 동의가 필요합니다.", Toast.LENGTH_SHORT).show()
            }

            // 아이디 확인
            if(registerBinding.btIdCheck.isEnabled)
            {
                Toast.makeText(this, "아이디 중복을 확인 해야합니다.", Toast.LENGTH_SHORT).show()
            }

            // 닉네임 설정 화면으로 이동
            if(!registerBinding.btIdCheck.isEnabled && registerBinding.btAgree.isChecked)
            {
                var index = intent.getIntExtra("index", 1)
                changeFragment(index)
            }
        }
    }

    // 아이디 확인
    fun validateEmail(): Boolean {
        val value = registerBinding.layoutId.editText?.text.toString()

        // 아이디 입력이 안된 경우
        return if (value.isEmpty()) {
            registerBinding.layoutId.error = "아이디를 입력해주세요."
            false
        }

        // 에러가 없는 경우
        else if (value.isNotEmpty()) {
            registerBinding.layoutId.error = null
            registerBinding.layoutId.isErrorEnabled = false // 에러 메시지 사용X
            true
        }

        else {
            true
        }
    }

    // 비밀번호 확인
    fun checkPw() : Boolean {

        val newPwd = registerBinding.layoutPw.editText?.text.toString()
        val checkPw = registerBinding.layoutPw2.editText?.text.toString()

        // 비밀번호 확인
        return if (newPwd != checkPw) {
            registerBinding.layoutPw2.error = "비밀번호가 일치하지 않습니다"
            false
        }
        // 에러가 없는 경우
        else {
            registerBinding.layoutPw2.error = null
            registerBinding.layoutPw2.isErrorEnabled = false // 에러 메시지 사용X
            true
        }
    }

    // 화면 이동
    fun changeFragment(index: Int){
        when(index) {
            // 닉네임 설정
            1 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(registerBinding.registerFrame.id, NameFragment())
                    .commitAllowingStateLoss()
            }
            // 프로필 설정
            2 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(registerBinding.registerFrame.id, IngredientFragment())
                    .commitAllowingStateLoss()
            }
        }
    }


    // 서버 연결 성공
    override fun onGetIdSuccess(response: IdCheckResponse) {
        Toast.makeText(this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
    }

    // 서버 연결 실패
    override fun onGetIdFailure(message: String) {
        Log.d("error", "오류 : $message")
    }
}
