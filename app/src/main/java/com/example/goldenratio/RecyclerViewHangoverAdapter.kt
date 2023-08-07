package com.example.goldenratio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.ItemBoardBinding

class RecyclerViewHangoverAdapter(private val hangoverList: List<HangoverData>) : RecyclerView.Adapter<RecyclerViewHangoverAdapter.MyViewHolder>() {
    private lateinit var itemListBinding: ItemBoardBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        itemListBinding = ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemListBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(hangoverList[position])

        //아이템 클릭 리스너
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(position)
        }

        //좋아요 버튼 리스너
        itemListBinding.buttonLike.setOnClickListener {
            itemClickListener.likeOnClick(position)
        }
    }

    override fun getItemCount(): Int = hangoverList.size

    inner class MyViewHolder(itemListBinding: ItemBoardBinding) : RecyclerView.ViewHolder(itemListBinding.root){
        fun bind(hangoverData: HangoverData) {
            with(itemListBinding) {
                //제목
                itemTitle.text = hangoverData.title
                //썸네일
                thumbnailBoard.setImageResource(hangoverData.thumbnail)
                //별점
                buttonRating.text = hangoverData.rating.toString()
                //좋아요
                buttonLike.text = hangoverData.like.toString()
                buttonLike.isChecked = hangoverData.likeCheck

                //좋아요 버튼 클릭 시 활성화
                buttonLike.setOnClickListener {
                    hangoverData.likeCheck = buttonLike.isChecked
                    notifyItemChanged(bindingAdapterPosition)
                }
            }
        }
    }
    //리스너 구현
    interface OnClickListener{
        fun onClick(position: Int)                  //아이템 클릭
        fun likeOnClick(position: Int)              //좋아요 클릭
    }

    fun setOnClickListener (onClickListener: OnClickListener) {
        this.itemClickListener = onClickListener
    }

    private lateinit var itemClickListener: OnClickListener
}