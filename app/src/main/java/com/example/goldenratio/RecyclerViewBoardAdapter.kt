package com.example.goldenratio

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.ItemBoardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class RecyclerViewBoardAdapter(private val boardList: ArrayList<BoardData>)
    :RecyclerView.Adapter<RecyclerViewBoardAdapter.CustomViewHolder>() {
    private lateinit var itemBoardBinding: ItemBoardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        itemBoardBinding = ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(itemBoardBinding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(boardList[position])

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

                CoroutineScope(Dispatchers.Main).launch {
                    val bitmap = withContext(Dispatchers.IO) {
                        convertBitmapFromURL(boardData.mainImageUrl)
                    }
                    thumbnailBoard.setImageBitmap(bitmap)
                }
                //별점
                buttonRating.text = boardData.averageScore.toString()

                //좋아요
                buttonLike.text = boardData.likeCount.toString()
                //buttonLike.isChecked = boardData.likeCheck

                //좋아요 버튼 클릭 시 활성화
                buttonLike.setOnClickListener {
                    //boardData.likeCheck = buttonLike.isChecked
                    boardData.likeCount++
                    notifyItemChanged(bindingAdapterPosition)
                }
            }
        }
    }

    private fun convertBitmapFromURL(url: String): Bitmap?{
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.doInput
        conn.connect()

        val input = conn.inputStream

        return BitmapFactory.decodeStream(input)
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