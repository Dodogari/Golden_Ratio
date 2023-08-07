package com.example.goldenratio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.goldenratio.databinding.ActivityHangoverItemBinding

class HangoverItemActivity : AppCompatActivity() {
    private lateinit var hangoverItemBinding: ActivityHangoverItemBinding
    private lateinit var recycleViewGradientAdapter: RecycleViewGradientAdapter
    private lateinit var recyclerViewReviewAdapter: ReviewAdapter
    private var gradientNameList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hangoverItemBinding = ActivityHangoverItemBinding.inflate(layoutInflater)
        setContentView(hangoverItemBinding.root)

        //
/*
        //세부 아이템 데이터 세팅
        //값 받아오기 - 서버 X
        val title = intent.getStringExtra("title")
        val thumbnail = intent.getIntExtra("thumbnail", 0)
        val rating = intent.getFloatExtra("rating", 2.8f)
        val like = intent.getIntExtra("like", 0)
        val writtenDate = intent.getStringExtra("writtenDate")
        val gradientList = intent.getParcelableArrayListExtra<GradientData>("gradientList")
        val recipeContent = intent.getStringExtra("recipeContent")
        var reviewList = intent.getParcelableArrayListExtra<ReviewData>("reviewList")
        val position = intent.getIntExtra("position", 0)

        hangoverItemBinding.itemBarTitle.text = title
        hangoverItemBinding.itemContentTitle.text = title
        hangoverItemBinding.thumbnailHangover.setImageResource(thumbnail)
        hangoverItemBinding.ratingScore7.text = rating.toString()
        hangoverItemBinding.ratingScore5.text = rating.toString()
        hangoverItemBinding.ratingCount4.text = "(${reviewList!!.size})"
        hangoverItemBinding.ratingCount5.text = "(${reviewList!!.size})"
        hangoverItemBinding.countLike.text = like.toString()
        hangoverItemBinding.timeUpload4.text = writtenDate
        hangoverItemBinding.recipeContent4.text = recipeContent

        //레시피
        //재료
        for (i in 0 until gradientList!!.size){
            gradientNameList.add(gradientList[i].gradientName)
        }

        hangoverItemBinding.materialCocktail.text = gradientNameList.joinToString(", ")

        //리사이클러뷰 레이아웃 설정 + 어댑터 연결
        recyclerViewReviewAdapter = ReviewAdapter(reviewList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        hangoverItemBinding.listReview4.layoutManager = layoutManager
        hangoverItemBinding.listReview4.adapter = recyclerViewReviewAdapter

        //이미지 출력
        recycleViewGradientAdapter = RecycleViewGradientAdapter(gradientList)

        hangoverItemBinding.gradientImageList.adapter = recycleViewGradientAdapter
        hangoverItemBinding.gradientImageList.setPadding(100, 100, 100, 100)

        hangoverItemBinding.gradientImageList.offscreenPageLimit = 3
        hangoverItemBinding.gradientImageList.getChildAt(0).overScrollMode= View.OVER_SCROLL_NEVER

        var transform = CompositePageTransformer()
        transform.addTransformer(MarginPageTransformer(30))

        transform.addTransformer(ViewPager2.PageTransformer{ view: View, fl: Float ->
            var v = 1-Math.abs(fl)
            view.scaleY = 0.8f + v * 0.2f
        })

        hangoverItemBinding.gradientImageList.setPageTransformer(transform)
        hangoverItemBinding.recipeContent4.text = recipeContent
        hangoverItemBinding.recipeRating6.rating = rating

        hangoverItemBinding.buttonBack.setOnClickListener {
            val itemIntent = Intent(this, HangoverFragment::class.java)
            setResult(RESULT_OK, itemIntent)
            if(!isFinishing) finish()
        }*/
    }
}