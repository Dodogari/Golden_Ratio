package com.example.goldenratio.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.goldenratio.databinding.FragmentNameBinding

var nick: String = ""

class NameFragment : Fragment() {

    private lateinit var nameBinding: FragmentNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        nameBinding = FragmentNameBinding.inflate(inflater, container, false)

        val activity = activity as RegisterActivity

        nameBinding.btNext.isEnabled = false

        nameBinding.etNick.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                nick = nameBinding.etNick.text.toString()
                nameBinding.btNext.isEnabled = nick.isNotEmpty()
            }
        })

        // 프로필 설정으로 이동
        nameBinding.btNext.setOnClickListener{
            //activity.changeFragment(2)
            val intent = Intent(getActivity(), ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        return nameBinding.root
    }
}