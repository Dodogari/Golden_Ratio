package com.example.goldenratio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goldenratio.databinding.ActivityReviewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat

class ReviewActivity : AppCompatActivity() {
    private lateinit var reviewBinding: ActivityReviewBinding
    private lateinit var reviewItemList: ArrayList<ReviewItemData>

    //리뷰 어댑터
    private lateinit var recyclerViewReviewAdapter: ReviewListAdapter
    private var total = 0f

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

                    //2-2. 어댑터 - 리스트 연결
                    recyclerViewReviewAdapter = ReviewListAdapter(reviewItemList)
                    reviewBinding.listReview.adapter = recyclerViewReviewAdapter

                    //2-3. 평균 점수 계산
                    for(i in 0 until reviewItemList.size) {
                        total += reviewItemList[i].rating
                    }

                    //1) 레이팅 바 표시
                    reviewBinding.avgRatingBar3.rating = total / reviewItemList.size
                    
                    //2) 텍스트 표시(소숫점 한 자리 수까지)
                    val ratingValue = DecimalFormat("#.#")
                    ratingValue.roundingMode = RoundingMode.HALF_UP
                    reviewBinding.avgRatingNum3.text = ratingValue.format(total / reviewItemList.size)

                    //2-4. 리뷰 갯수
                    reviewBinding.ratingCount3.text = "(${reviewItemList.size})"
                }
            }

            override fun onFailure(call: Call<ArrayList<ReviewItemData>>, t: Throwable) {
                Toast.makeText(this@ReviewActivity, "데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
            }

        })

        //#3. back button
        reviewBinding.buttonBack.setOnClickListener {
            startActivity(Intent(this@ReviewActivity, CocktailItemActivity::class.java))

            //끝나지 않았다면
            if (!isFinishing) finish()
        }

        //#4. 등록 버튼
        reviewBinding.reviewEnroll.setOnClickListener {
            val registerData = ReviewRegisterData(reviewBinding.writingContent.text.toString(), reviewBinding.ratingBar.rating)

            //4-1. 통신
            val registerReviewContent = RegisterClient.reviewService.registerReview(boardId, registerData)
            registerReviewContent.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful.not())
                        Toast.makeText(this@ReviewActivity, response.message(), Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this@ReviewActivity, "데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@ReviewActivity, "데이터 전송이 실패되었습니다.", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
}