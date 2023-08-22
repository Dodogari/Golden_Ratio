package com.example.goldenratio

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

        @SuppressLint("SetTextI18n")
        fun bind(reviewItemData: ReviewItemData) {
            with(reviewItemBinding) {
                userName.text = "사용자 ${reviewItemData.reviewer}"                //작성자명
                userRating.rating = reviewItemData.rating               //별점
                reviewContent.text = reviewItemData.comment             //내용

                try {
                    Glide.with(userProfile)
                        .load(reviewItemData.profileImageUrl)
                        .into(userProfile)
                }
                catch (e: Exception) {

                }
            }
        }
    }
}
