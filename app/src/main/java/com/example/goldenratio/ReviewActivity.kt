package com.example.goldenratio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.config.ApplicationClass.Companion.X_ACCESS_TOKEN
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
        //1-1. 데이터 포지션 / 카테고리
        val boardId = intent.getIntExtra("boardId", 0)
        val category = intent.getIntExtra("category", -1)

        reviewBinding.ratingCount3.text = "(0)"
        reviewBinding.avgRatingNum3.text = "0.0"

        Toast.makeText(this@ReviewActivity, boardId.toString(), Toast.LENGTH_SHORT).show()

        try {
            //1-2. 통신
            val reviewContent = RegisterClient.reviewService.getReviewAll(boardId.toString())
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
        } catch (e: NullPointerException) {
        }

        //#3. back button
        reviewBinding.buttonBack.setOnClickListener {
            var itemIntent = Intent()
            if(category == 0) {
                itemIntent = Intent(this@ReviewActivity, CocktailItemActivity::class.java)
            }
            else if(category == 1) {
                itemIntent = Intent(this@ReviewActivity, HangoverItemActivity::class.java)
            }

            itemIntent.putExtra("boardId", boardId)
            startActivity(itemIntent)

            //끝나지 않았다면
            if (!isFinishing) finish()
        }

        //#4. 등록 버튼
        reviewBinding.reviewEnroll.setOnClickListener {
            val registerData = ReviewRegisterData(reviewBinding.writingContent.text.toString(), reviewBinding.ratingBar.rating)

            //4-1. 통신
            val registerReviewContent = RegisterClient.reviewService.registerReview(boardId.toString(),
                "Bearer $X_ACCESS_TOKEN", registerData)
            registerReviewContent.enqueue(object : Callback<PostResponse> {
                override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {

                    Toast.makeText(this@ReviewActivity, response.code().toString(), Toast.LENGTH_SHORT).show()
                    if (response.isSuccessful) {
                        val result = response.body()!!.result
                        Toast.makeText(this@ReviewActivity, result, Toast.LENGTH_SHORT).show()

                        //화면 갱신 : 종료 후 다시 시작
                        finish()
                        overridePendingTransition(0, 0) //인텐트 애니메이션 없애기

                        startActivity(intent) //현재 액티비티 재실행 실시
                        overridePendingTransition(0, 0) //인텐트 애니메이션 없애기
                    }
                }

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    Toast.makeText(this@ReviewActivity, "리뷰 등록을 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
}