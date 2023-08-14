package com.example.goldenratio.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.ItemSearchRankBinding

class RankAdapter(private val rankList : ArrayList<Rank>): RecyclerView.Adapter<RankAdapter.ViewHolder>() {

    inner class ViewHolder(val rankBinding: ItemSearchRankBinding): RecyclerView.ViewHolder(rankBinding.root) {
        fun bind (rank: Rank) {
            rankBinding.imgRank.setImageResource(rankList[position].rankImg)
            rankBinding.tvRank.text = rankList.get(position).rank
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rankBinding =  ItemSearchRankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(rankBinding)
    }

    override fun getItemCount(): Int = rankList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(rankList[position])
    }
}
