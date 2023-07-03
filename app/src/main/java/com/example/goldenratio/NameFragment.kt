package com.example.goldenratio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.goldenratio.databinding.FragmentNameBinding

class NameFragment : Fragment() {
    private lateinit var nameBinding: FragmentNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nameBinding = FragmentNameBinding.inflate(inflater, container, false)
        return nameBinding.root
    }
}