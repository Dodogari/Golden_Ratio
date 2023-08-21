package com.example.goldenratio.cocktail.models

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.ItemTextBinding


class TvAdapter(private val tvList: ArrayList<TvRatio>): RecyclerView.Adapter<TvAdapter.ViewHolder>() {

    inner class ViewHolder(val tvBinding: ItemTextBinding): RecyclerView.ViewHolder(tvBinding.root) {
        fun bind (tvRatio: TvRatio) {
            tvBinding.tvRatio.text = tvRatio.name
            tvBinding.tvRatio.setTextColor(Color.parseColor(tvRatio.color))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val tvBinding =  ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(tvBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tvList[position])
    }

    override fun getItemCount(): Int = tvList.size
}