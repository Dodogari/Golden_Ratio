package com.example.goldenratio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.ViewUpperSlideBinding

class PagerRecyclerAdapter(private val imgList: List<Int>) : RecyclerView.Adapter<PagerRecyclerAdapter.ViewHolder>() {
    private lateinit var pagerRecyclerBinding : ViewUpperSlideBinding

    //가상의 첫번째 뷰와 마지막 뷰 추가
    private val showList : List<Int> = listOf(imgList.last()) + imgList + listOf(imgList.first())

    inner class ViewHolder(pagerRecyclerBinding: ViewUpperSlideBinding) : RecyclerView.ViewHolder(pagerRecyclerBinding.root){
        val cardView = pagerRecyclerBinding.imgView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        pagerRecyclerBinding = ViewUpperSlideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(pagerRecyclerBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardView.setImageResource(showList[position % imgList.size])
    }

    override fun getItemCount(): Int = imgList.size
}