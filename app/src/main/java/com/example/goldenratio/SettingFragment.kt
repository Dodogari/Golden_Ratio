package com.example.goldenratio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.goldenratio.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private lateinit var settingBinding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingBinding = FragmentSettingBinding.inflate(inflater, container, false)
        return settingBinding.root
    }
}