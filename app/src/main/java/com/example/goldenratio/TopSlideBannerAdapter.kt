package com.example.goldenratio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.TopSlideBannerBinding

//상단 슬라이드 배너 어댑터
class TopSlideBannerAdapter(private val slideList: List<Int>) : RecyclerView.Adapter<TopSlideBannerAdapter.ViewHolder>() {
    private lateinit var topSlideBannerBinding : TopSlideBannerBinding

    //가상의 첫번째 뷰와 마지막 뷰 추가
    private val showList : List<Int> = listOf(slideList.last()) + slideList + listOf(slideList.first())

    //레이아웃에 정의된 상단 슬라이드 배너
    inner class ViewHolder(topSlideBannerBinding: TopSlideBannerBinding) : RecyclerView.ViewHolder(topSlideBannerBinding.root){
        val topSlideImage = topSlideBannerBinding.topSlideBannerImage
    }

    //레이아웃 객체화(inflate)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        topSlideBannerBinding = TopSlideBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(topSlideBannerBinding)
    }

    //상단 슬라이드 배너 이미지가 연속되게 보여짐
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.topSlideImage.setImageResource(showList[position % slideList.size])
    }

    //상단 슬라이드 배너 이미지 갯수
    override fun getItemCount(): Int = slideList.size
}