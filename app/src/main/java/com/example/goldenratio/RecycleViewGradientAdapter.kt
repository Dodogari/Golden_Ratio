package com.example.goldenratio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.GradientImageItemBinding

class RecycleViewGradientAdapter(var gradientList: List<RecipeData>)
    : RecyclerView.Adapter<RecycleViewGradientAdapter.ViewHolder>() {
    private lateinit var gradientImageItemBinding: GradientImageItemBinding

    override fun getItemCount(): Int = gradientList.size

    inner class ViewHolder(gradientImageItemBinding: GradientImageItemBinding) : RecyclerView.ViewHolder(gradientImageItemBinding.root) {
        val cardView = gradientImageItemBinding.gradientImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        gradientImageItemBinding = GradientImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(gradientImageItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.cardView.setImageResource(gradientList[position].gradientImageUrl)
    }
}