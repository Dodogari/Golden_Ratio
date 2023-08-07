package com.example.goldenratio.hangover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.R

class ImgAdapter(val imgList : ArrayList<Img>) : RecyclerView.Adapter<ImgAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pic_list, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.img.setImageResource(imgList[position].img)
    }

    class CustomViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.img_pic) // 사진
    }
}
