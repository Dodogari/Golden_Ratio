package com.example.goldenratio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.GradientData
import com.example.goldenratio.databinding.GradientImageItemBinding

class RecycleViewGradientAdapter(var gradientList: List<GradientData>)
    : RecyclerView.Adapter<RecycleViewGradientAdapter.CustomViewHolder>() {
    private lateinit var gradientImageItemBinding: GradientImageItemBinding

    override fun getItemCount(): Int = gradientList.size

    inner class CustomViewHolder(gradientImageItemBinding: GradientImageItemBinding) : RecyclerView.ViewHolder(gradientImageItemBinding.root) {
        val cardView = gradientImageItemBinding.gradientImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        gradientImageItemBinding = GradientImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(gradientImageItemBinding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        with(gradientImageItemBinding) {
            Glide.with(gradientImage)
                .load(gradientList[position].gradientImageUrl)
                .into(gradientImage)
        }
    }
}