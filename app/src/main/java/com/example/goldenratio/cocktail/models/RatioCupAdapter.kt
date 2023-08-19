package com.example.goldenratio.cocktail.models

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.ItemRatioCupBinding

class RatioCupAdapter(private val ratioCupList: ArrayList<RatioCup>): RecyclerView.Adapter<RatioCupAdapter.ViewHolder>() {

    var itemClick: ItemClick? = null

    inner class ViewHolder(val ratioCupBinding: ItemRatioCupBinding): RecyclerView.ViewHolder(ratioCupBinding.root) {
        fun bind (RatioCup: RatioCup) {
            ratioCupBinding.tvRatio.setBackgroundColor(Color.parseColor(RatioCup.color))
            ratioCupBinding.tvRatio.setTextSize(RatioCup.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val ratioCupBinding =  ItemRatioCupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(ratioCupBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ratioCupList[position])
    }

    override fun getItemCount(): Int = ratioCupList.size

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }
}