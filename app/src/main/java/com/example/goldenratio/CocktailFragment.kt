package com.example.goldenratio

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.goldenratio.databinding.FragmentCocktailBinding
import me.relex.circleindicator.CircleIndicator3

class CocktailFragment : Fragment() {
    private lateinit var cocktailBinding: FragmentCocktailBinding
    //상단 슬라이드 관련 변수
    private var slideList: MutableList<Int> = mutableListOf()
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: PagerRecyclerAdapter
    private lateinit var indicator3: CircleIndicator3
    private lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cocktailBinding = FragmentCocktailBinding.inflate(inflater, container, false)
        return cocktailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //slide 리스트에 상단 슬라이드에 비출 이미지 저장
        slideList.add(R.drawable.view_test1)
        slideList.add(R.drawable.view_text2)
        slideList.add(R.drawable.view_test3)

        //핸들러 설정
        handler = Handler(Looper.myLooper()!!)

        //viewPager2 설정
        viewPager2 = cocktailBinding.upperSlide
        adapter = PagerRecyclerAdapter(slideList)
        viewPager2.adapter = adapter

        //indicator 연결
        indicator3 = cocktailBinding.indicator
        indicator3.setViewPager(viewPager2)

        //자동 슬라이드 대기 시간 설정
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                //스크롤 상태일 때
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    //범위를 초과할 때 설정
                    when (viewPager2.currentItem) {
                        //오른쪽으로 스크롤하다 끝까지 도달해 범위를 넘어간다면 맨 첫번째 사진 뷰를 보여줌
                        (slideList.size + 1) -> viewPager2.setCurrentItem(0, false)
                        //왼쪽으로 스크롤하다 끝까지 도달해 범위를 넘어간다면 맨 마지막 사진 뷰를 보여줌
                        -1 -> viewPager2.setCurrentItem(slideList.size, false)
                    }
                }
            }
            //자동 슬라이드 설정
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //인디케이터 연결
                indicator3.animatePageSelected(position)
                //슬라이드 시간 설정 -> 3초에 한번 스크롤
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 3000)
            }
        })
    }

    //자동 회전 시 크기를 넘어가면
    private val runnable = Runnable {
        if (viewPager2.currentItem == slideList.size - 1)
            viewPager2.currentItem = 0
        else
            viewPager2.currentItem = viewPager2.currentItem + 1
    }

    //Resume 상태라면 현재 페이지에서 3초 대기
    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 3000)
    }
}