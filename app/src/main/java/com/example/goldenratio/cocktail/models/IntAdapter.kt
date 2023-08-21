package com.example.goldenratio.cocktail.models

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.cocktail.ratioColorList
import com.example.goldenratio.cocktail.ratioIntList
import com.example.goldenratio.cocktail.ratioList
import com.example.goldenratio.cocktail.ratioNameList
import com.example.goldenratio.databinding.ItemTextBinding


class IntAdapter(private val intList: ArrayList<TvRatio>): RecyclerView.Adapter<IntAdapter.ViewHolder>() {

    inner class ViewHolder(val intBinding: ItemTextBinding): RecyclerView.ViewHolder(intBinding.root) {
        fun bind (tvRatio: TvRatio) {
            intBinding.tvRatio.text = tvRatio.name
            intBinding.tvRatio.setTextColor(Color.parseColor(tvRatio.color))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val tvBinding =  ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(tvBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(intList[position])
        for (i in 0 ..ratioNameList.size -1) {
            if (ratioNameList.size == 0) {
                holder.intBinding.tvEct.isVisible = false
            }
        }
    }

    override fun getItemCount(): Int = intList.size
}