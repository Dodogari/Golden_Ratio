package com.example.goldenratio

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.goldenratio.databinding.FragmentSettingBinding
import com.example.goldenratio.login.LoginActivity

class SettingFragment : Fragment() {
    private lateinit var settingBinding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingBinding = FragmentSettingBinding.inflate(inflater, container, false)
        return settingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingBinding.agreement.setOnClickListener {
            startActivity(Intent(context, AgreementActivity::class.java))
        }

        settingBinding.logOut.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
            Toast.makeText(getActivity(),"로그아웃 되었습니다.",Toast.LENGTH_SHORT).show()
        }
    }
}