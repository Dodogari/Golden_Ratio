package com.example.goldenratio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.goldenratio.databinding.FragmentHangoverBinding

class HangoverFragment : Fragment() {
    private lateinit var hangoverBinding: FragmentHangoverBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hangoverBinding = FragmentHangoverBinding.inflate(inflater, container, false)
        return hangoverBinding.root
    }
}