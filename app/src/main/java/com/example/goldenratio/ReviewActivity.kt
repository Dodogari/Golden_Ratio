package com.example.goldenratio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.ActivityReviewBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class ReviewActivity : AppCompatActivity() {
    private lateinit var reviewBinding: ActivityReviewBinding
    private lateinit var recyclerViewReviewAdapter: ReviewAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reviewBinding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(reviewBinding.root)

        //콘텐츠 내용 값 가져오기
        /*val reviewList = intent.getParcelableArrayListExtra<ReviewData>("reviewList")

        recyclerViewReviewAdapter = ReviewAdapter(reviewList!!)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        reviewBinding.listReview.layoutManager = layoutManager
        reviewBinding.listReview.adapter = recyclerViewReviewAdapter

        recyclerViewReviewAdapter.notifyItemRangeChanged(reviewList.size, reviewList.size)

        var total = 0

        for(i in 0 until reviewList.size) {
            total += reviewList[i].rating
        }
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.DOWN

        var avg = df.format(total / (reviewList.size).toFloat())

        reviewBinding.avgRatingBar3.rating = avg.toFloat()
        reviewBinding.ratingCount3.text = "(${reviewList.size})"
        reviewBinding.avgRatingNum3.text = avg

        reviewBinding.reviewEnroll.setOnClickListener {
            val rate = reviewBinding.ratingBar.rating
            val content = reviewBinding.writingContent.text.toString()
            reviewList.add(ReviewData("황금비율", R.drawable.egg, rate.toInt(), content))

            recyclerViewReviewAdapter.notifyItemRangeChanged(reviewList.size, 1)
            total += rate.toInt()
            avg = df.format(total / (reviewList.size).toFloat())
            reviewBinding.avgRatingBar3.rating = avg.toFloat()
            reviewBinding.ratingCount3.text = "(${reviewList.size})"
            reviewBinding.avgRatingNum3.text = avg

            reviewBinding.ratingBar.rating = 0f
            reviewBinding.writingContent.text.clear()

            Toast.makeText(this, "성공적으로 등록하였습니다.", Toast.LENGTH_SHORT).show()
        }

        //원래 화면으로 돌아가기
        reviewBinding.buttonBack.setOnClickListener {
            val itemIntent = Intent(this@ReviewActivity, CocktailItemActivity::class.java)
            itemIntent.putExtra("total", avg)
            itemIntent.putParcelableArrayListExtra("reviewList", reviewList)

            //결과값 반환
            setResult(RESULT_OK, itemIntent)

            //끝나지 않았다면
            if (!isFinishing) finish()
        }*/
    }
}