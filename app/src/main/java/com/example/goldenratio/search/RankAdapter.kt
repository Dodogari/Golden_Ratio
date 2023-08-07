package com.example.goldenratio.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.R

class RankAdapter(val rankList : ArrayList<Rank>) : RecyclerView.Adapter<RankAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_rank, parent, false)

        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rankList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.rankImg.setImageResource(rankList[position].rankImg)
        holder.rankTv.text = rankList.get(position).rank
    }

    class CustomViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView) {
        val rankImg = itemView.findViewById<ImageView>(R.id.img_rank) // 순위
        val rankTv = itemView.findViewById<TextView>(R.id.tv_rank) // 인기 검색어
    }
}
