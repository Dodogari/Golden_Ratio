package com.example.goldenratio.cocktail.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.goldenratio.databinding.ItemPicListBinding
import com.example.goldenratio.hangover.Img
import com.example.goldenratio.cocktail.ingredientList

class ImgAdapter2(private val imgList: ArrayList<Img>): RecyclerView.Adapter<ImgAdapter2.ViewHolder>() {

    inner class ViewHolder(val imgBinding: ItemPicListBinding): RecyclerView.ViewHolder(imgBinding.root) {
        fun bind (img: Img) {
            Glide.with(imgBinding.imgPic)
                .load(ingredientList[position].img.toString()) // 불러올 이미지 url
                .into(imgBinding.imgPic) // 이미지를 넣을 뷰
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imgBinding =  ItemPicListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(imgBinding)
    }

    override fun getItemCount(): Int = imgList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imgList[position])
    }
}
