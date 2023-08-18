package com.example.goldenratio.cocktail.models

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.ItemRatioBinding

class RatioAdapter(private val ratioList: ArrayList<Ratio>): RecyclerView.Adapter<RatioAdapter.ViewHolder>() {

    var itemClick: ItemClick? = null

    inner class ViewHolder(val ratioBinding: ItemRatioBinding): RecyclerView.ViewHolder(ratioBinding.root) {
        fun bind (Ratio: Ratio) {
            ratioBinding.tvName.text = Ratio.name

            ratioBinding.tvIngredient.setBackgroundColor(Color.parseColor(Ratio.color))

            var ratio = 1

            ratioBinding.btRatio.setOnClickListener {
                ratio++
                ratioBinding.tvRatio.text = ratio.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding =  ItemRatioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ratioList[position])

        if (itemClick != null) {
            holder.ratioBinding!!.btRatio.setOnClickListener(View.OnClickListener {
                itemClick?.onClick(it, position)
            })
        }
    }

    override fun getItemCount(): Int = ratioList.size

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }
}