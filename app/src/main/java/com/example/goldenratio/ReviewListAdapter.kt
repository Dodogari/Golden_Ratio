package com.example.goldenratio

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.ReviewListBinding
import java.net.HttpURLConnection
import java.net.URL

class ReviewListAdapter(private val reviewList: ArrayList<ReviewItemData>)
    :RecyclerView.Adapter<ReviewListAdapter.CustomViewHolder>() {
    private lateinit var reviewItemBinding: ReviewListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        reviewItemBinding = ReviewListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(reviewItemBinding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }

    override fun getItemCount(): Int = reviewList.size

    inner class CustomViewHolder(reviewItemBinding: ReviewListBinding) : RecyclerView.ViewHolder(reviewItemBinding.root){

        fun bind(reviewItemData: ReviewItemData) {
            with(reviewItemBinding) {
                userName.text = reviewItemData.reviewer                 //작성자명
                userRating.rating = reviewItemData.rating               //별점
                reviewContent.text = reviewItemData.comment             //내용
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
}
