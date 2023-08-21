package com.example.goldenratio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.goldenratio.databinding.ItemBoardBinding

class RecyclerViewBoardAdapter(private val boardList: ArrayList<BoardData>, private val markList: ArrayList<Boolean>)
    :RecyclerView.Adapter<RecyclerViewBoardAdapter.CustomViewHolder>() {
    private lateinit var itemBoardBinding: ItemBoardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        itemBoardBinding = ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(itemBoardBinding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(boardList[position])

        //sharedPreferences 에 저장되어 있는 상태에 따라 ON/OFF 초기화
        itemBoardBinding.buttonLike.isChecked = markList[position]

        //아이템 클릭 리스너
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(position)
        }
        
        //좋아요 버튼 리스너
        itemBoardBinding.buttonLike.setOnClickListener {
            itemClickListener.likeOnClick(position)
        }
    }

    override fun getItemCount(): Int = boardList.size

    inner class CustomViewHolder(itemBoardBinding: ItemBoardBinding) : RecyclerView.ViewHolder(itemBoardBinding.root){

        fun bind(boardData: BoardData) {
            with(itemBoardBinding) {
                itemTitle.text = boardData.title                        //제목

                //이미지
                Glide.with(thumbnailBoard)
                    .load(boardData.mainImageUrl) // 불러올 이미지 url
                    .into(thumbnailBoard) // 이미지를 넣을 뷰

                //별점
                buttonRating.text = boardData.averageScore.toString()

                //좋아요
                buttonLike.text = boardData.likeCount.toString()

                //좋아요 버튼 클릭 시 활성화
                buttonLike.setOnClickListener {
                    buttonLike.isChecked = !buttonLike.isChecked
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