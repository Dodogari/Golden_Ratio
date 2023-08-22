package com.example.goldenratio.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.ItemNameSearchBinding

class SearchNameAdapter(private val searchNameList: ArrayList<SearchName>): RecyclerView.Adapter<SearchNameAdapter.ViewHolder>() {

    inner class ViewHolder(val searchNameBinding: ItemNameSearchBinding): RecyclerView.ViewHolder(searchNameBinding.root) {
        fun bind (searchName: SearchName) {
            searchNameBinding.btSearch.setImageResource(searchNameList[position].search)
            searchNameBinding.tvSearch.text = searchNameList.get(position).name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val searchBinding =  ItemNameSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(searchBinding)
    }

    override fun getItemCount(): Int = searchNameList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchNameList[position])

        holder.searchNameBinding!!.btSearch.setOnClickListener{
            search(position)
        }
    }

    fun search(position: Int) {
        if (position >= 0) {
            notifyDataSetChanged()
        }
    }
}
