package com.example.goldenratio.cocktail.models

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.cocktail.ratioNameList
import com.example.goldenratio.databinding.ItemRatioCheckBinding


class AddRatioAdapter(private val ratioList: ArrayList<Ratio>): RecyclerView.Adapter<AddRatioAdapter.ViewHolder>() {

    inner class ViewHolder(val checkBinding: ItemRatioCheckBinding): RecyclerView.ViewHolder(checkBinding.root) {
        fun bind (Ratio: Ratio) {

            checkBinding.tvName.text = Ratio.name
            checkBinding.tvIngredient.setBackgroundColor(Color.parseColor(Ratio.color))

            val checkName = checkBinding.tvName.text.toString()

            // 재료 선택
            checkBinding.btCheck.setOnClickListener {
                ratioNameList.apply {
                    if (checkName != null)
                    {
                        add(checkName)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding =  ItemRatioCheckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ratioList[position])
    }

    override fun getItemCount(): Int = ratioList.size

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }
}