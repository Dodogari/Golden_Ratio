package com.example.goldenratio.cocktail.models

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.cocktail.*
import com.example.goldenratio.databinding.ItemRatioBinding


class RatioAdapter(private val ratioList: ArrayList<Ratio>): RecyclerView.Adapter<RatioAdapter.ViewHolder>() {

    inner class ViewHolder(val ratioBinding: ItemRatioBinding): RecyclerView.ViewHolder(ratioBinding.root) {
        fun bind (Ratio: Ratio) {
            ratioBinding.tvName.text = Ratio.name
            ratioBinding.tvIngredient.setBackgroundColor(Color.parseColor(Ratio.color))
            ratioBinding.tvRatio.text = ratioIntList.get(position).toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding =  ItemRatioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ratioList[position])

        holder.ratioBinding!!.btRatio.setOnClickListener(View.OnClickListener {
            intRatio = holder.ratioBinding.tvRatio.text.toString().toInt()
            if(intRatio < 5) {
                intRatio++
            }else{
                intRatio = 0
            }
            setRatio(position)
        })
    }

    override fun getItemCount(): Int = ratioList.size

    // 비율 설정
    fun setRatio(position: Int) {
        for (i in 0 ..ratioNameList.size -1) {
            if (ratioNameList.size != 0) {
                ratioIntList[position] = intRatio
                notifyDataSetChanged()
            }
        }
        Log.d("tag", "intRatio: {$intRatio}")
    }
}