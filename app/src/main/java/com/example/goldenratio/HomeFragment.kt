package com.example.goldenratio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.goldenratio.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private lateinit var homeBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //상단 탭 설정
        //탭 메뉴 추가
        val menuList = listOf("칵테일", "숙취해소")
        val tabLayoutMediator =
            TabLayoutMediator(homeBinding.navigationTab, homeBinding.viewSlide) { tab, pos ->
                tab.text = menuList[pos]
            }
        //탭 어댑터 연결
        homeBinding.viewSlide.adapter = PagerAdapter(this)
        tabLayoutMediator.attach()
    }
}