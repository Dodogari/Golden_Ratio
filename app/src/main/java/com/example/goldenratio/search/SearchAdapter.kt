package com.example.goldenratio.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.R

class SearchAdapter(val searchList : ArrayList<Search>) : RecyclerView.Adapter<SearchAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_search, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.searchTv.text = searchList.get(position).search
        holder.del.setImageResource(searchList[position].del)
    }

    class CustomViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView) {
        val searchTv = itemView.findViewById<TextView>(R.id.tv_search) // 사용자 검색어
        val del = itemView.findViewById<ImageButton>(R.id.bt_del) // 삭제
    }
}
