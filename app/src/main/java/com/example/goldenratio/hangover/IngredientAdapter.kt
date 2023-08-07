package com.example.goldenratio.hangover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.R

class IngredientAdapter(val ingredientList : ArrayList<Ingredient>) : RecyclerView.Adapter<IngredientAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.img.setImageURI(img_uri)
        holder.name.text = ingredientList.get(position).name
        holder.del.setImageResource(ingredientList[position].del)
    }

    class CustomViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.img_ingredient) // 사진
        val name = itemView.findViewById<TextView>(R.id.tv_name)         // 이름
        val del = itemView.findViewById<ImageButton>(R.id.bt_del)        // 삭제
    }
}
