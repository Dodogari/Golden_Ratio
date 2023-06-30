package com.example.goldenratio

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment
    = when (position) {
        0 -> CocktailFragment()
        1 -> HangoverFragment()
        else -> CocktailFragment()
    }
}