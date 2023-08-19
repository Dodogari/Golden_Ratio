package com.example.goldenratio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goldenratio.databinding.ActivityReviewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewActivity : AppCompatActivity() {
    private lateinit var reviewBinding: ActivityReviewBinding
    private lateinit var reviewItemList: ArrayList<ReviewItemData>

    //리뷰 어댑터
    private lateinit var recyclerViewReviewAdapter: ReviewListAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding 정의
        reviewBinding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(reviewBinding.root)

        //#1. 서버 통신: 세부 칵테일 보드 내용 받아오기
        //1-1. 데이터 포지션
        val boardId = intent.getIntExtra("boardId", 1).toString()

        //1-2. 통신
        val reviewContent = RegisterClient.reviewService.getReviewAll(boardId)
        reviewContent.enqueue(object : Callback<ArrayList<ReviewItemData>> {
            override fun onResponse(
                call: Call<ArrayList<ReviewItemData>>,
                response: Response<ArrayList<ReviewItemData>>
            ) {
                if(response.isSuccessful) {
                    reviewItemList = response.body()!!

                    //#2. 리사이클러뷰 설정
                    //2-1. 리사이클러뷰 레이아웃 설정
                    reviewBinding.listReview.layoutManager = LinearLayoutManager(this@ReviewActivity, LinearLayoutManager.VERTICAL, false)

                    //3-2. 어댑터 - 리스트 연결
                    recyclerViewReviewAdapter = ReviewListAdapter(reviewItemList)
                    reviewBinding.listReview.adapter = recyclerViewReviewAdapter
                }
            }

            override fun onFailure(call: Call<ArrayList<ReviewItemData>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        //#3. back button
        reviewBinding.buttonBack.setOnClickListener {
            startActivity(Intent(this@ReviewActivity, CocktailItemActivity::class.java))

            //끝나지 않았다면
            if (!isFinishing) finish()
        }
    }
}