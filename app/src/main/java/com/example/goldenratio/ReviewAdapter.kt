package com.example.goldenratio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.ReviewListBinding

class ReviewAdapter(private var reviewList: ArrayList<ReviewData>)
    : RecyclerView.Adapter<ReviewAdapter.MyViewHolder>() {
    private lateinit var reviewAdapterBinding : ReviewListBinding

    //뷰홀더가 생성됐을 때
    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int) : MyViewHolder {
        //연결할 레이아웃
        reviewAdapterBinding = ReviewListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(reviewAdapterBinding)
    }

    //데이터를 묶었을 때
    override fun onBindViewHolder(viewHolder : MyViewHolder, position : Int) {
        viewHolder.bind(reviewList[position])
    }

    //아이템 갯수
    override fun getItemCount(): Int = reviewList.size

    //클래스 내부 객체 -> ViewHolder
    inner class MyViewHolder(reviewListBinding: ReviewListBinding) : RecyclerView.ViewHolder(reviewListBinding.root) {

        //실제 레이아웃에 데이터를 넣기
        fun bind(reviewData: ReviewData) {
            with(reviewAdapterBinding) {
                //텍스트 : 사용자 이름, 리뷰 내용
                userName.text = reviewData.userName
                reviewContent.text = reviewData.content

                //프로필 이미지
                userProfile.setImageResource(reviewData.userProfile)

                //최초 별점
                userRating.rating = reviewData.rating.toFloat()
            }
        }
    }
}