package com.example.goldenratio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.ItemListBinding

class RecyclerViewBoardAdapter(private val getFragment: Fragment, private val boardList: List<BoardData>)
    :RecyclerView.Adapter<RecyclerViewBoardAdapter.MyViewHolder>() {
    private lateinit var itemListBinding: ItemListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        itemListBinding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemListBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(boardList[position])
        
        //좋아요 버튼 리스너
        itemListBinding.buttonLike.setOnClickListener {
            itemListBinding.buttonLike.isChecked = !itemListBinding.buttonLike.isChecked
        }
    }

    override fun getItemCount(): Int = boardList.size

    inner class MyViewHolder(itemListBinding: ItemListBinding) : RecyclerView.ViewHolder(itemListBinding.root){
        fun bind(boardData: BoardData) {
            with(itemListBinding) {
                //제목
                itemTitle.text = boardData.title
                //썸네일
                pictureCocktail.setImageResource(boardData.thumbnail)
                //별점
                buttonRating.text = boardData.rating.toString()
                //좋아요
                buttonLike.text = boardData.like.toString()
                buttonLike.isChecked = boardData.likeCheck

                //상태 버튼 활성화
                buttonLike.setOnClickListener {
                    boardData.likeCheck = buttonLike.isChecked
                    notifyItemChanged(adapterPosition)
                }
            }
        }

    }
}