package com.example.goldenratio.hangover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.goldenratio.cocktail.ingredientNameList
import com.example.goldenratio.databinding.ItemIngredientBinding

class IngredientAdapter(private val ingredientList: ArrayList<Ingredient>): RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    inner class ViewHolder(val ingredientBinding: ItemIngredientBinding): RecyclerView.ViewHolder(ingredientBinding.root) {
        fun bind (Ingredient: Ingredient) {
            Glide.with(ingredientBinding.imgIngredient)
                .load(ingredientList[position].img.toString()) // 불러올 이미지 url
                .into(ingredientBinding.imgIngredient) // 이미지를 넣을 뷰

            ingredientBinding.tvName.text = ingredientList.get(position).name
            ingredientBinding.btDel.setImageResource(ingredientList[position].del)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val ingredientBinding =  ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(ingredientBinding)
    }

    override fun getItemCount(): Int = ingredientList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ingredientList[position])

        holder.ingredientBinding!!.btDel.setOnClickListener{
            delete(position)
        }
    }

    fun delete(position: Int) {
        if (position >= 0) {
            ingredientList.removeAt(position)
            ingredientNameList.removeAt(position)
            notifyDataSetChanged()
        }
    }
}
