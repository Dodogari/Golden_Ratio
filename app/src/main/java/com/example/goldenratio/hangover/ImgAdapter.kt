package com.example.goldenratio.hangover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.ItemPicListBinding

class ImgAdapter(private val imgList: ArrayList<Img>): RecyclerView.Adapter<ImgAdapter.ViewHolder>() {

    inner class ViewHolder(val imgBinding: ItemPicListBinding): RecyclerView.ViewHolder(imgBinding.root) {
        fun bind (img: Img) {
            imgBinding.imgPic.setImageURI(imgList[position].img)
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
