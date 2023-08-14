package com.example.goldenratio.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.ItemUserSearchBinding

class SearchAdapter(private val searchList: ArrayList<Search>): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(val searchBinding: ItemUserSearchBinding): RecyclerView.ViewHolder(searchBinding.root) {
        fun bind (search: Search) {
            searchBinding.tvSearch.text = searchList.get(position).search
            searchBinding.btDel.setImageResource(searchList[position].del)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val searchBinding =  ItemUserSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(searchBinding)
    }

    override fun getItemCount(): Int = searchList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchList[position])

        holder.searchBinding!!.btDel.setOnClickListener{
            delete(position)
        }
    }

    fun delete(position: Int) {
        if (position >= 0) {
            searchList.removeAt(position)
            notifyDataSetChanged()
        }
    }
}
