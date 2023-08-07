package com.example.goldenratio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.goldenratio.databinding.ActivityCocktailItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CocktailItemActivity : AppCompatActivity() {
    private lateinit var cocktailItemBinding: ActivityCocktailItemBinding
    private lateinit var recycleViewGradientAdapter: RecycleViewGradientAdapter
    private lateinit var recyclerViewReviewAdapter: ReviewAdapter
    private lateinit var itemResultLauncher: ActivityResultLauncher<Intent>
    private var materialNameList2 = mutableListOf<String>()
    private var materialNameList = mutableListOf<String>()
    private var materialNumList = mutableListOf<Int>()

    private lateinit var cocktailItem: CocktailData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding 정의
        cocktailItemBinding = ActivityCocktailItemBinding.inflate(layoutInflater)
        setContentView(cocktailItemBinding.root)

        //#1. 서버 통신: 세부 칵테일 보드 내용 받아오기
        //1-1. 데이터 포지션
        val boardId = intent.getIntExtra("boardId", 1)

        Log.d("dd", boardId.toString())

        //1-2. 통신
        /*val cocktailItemContent = RegisterClient.registerService.getCocktailItem(boardId,)
        cocktailItemContent.enqueue(object : Callback<CocktailData> {
            //서버 응답 시
            override fun onResponse(
                call: Call<CocktailData>,
                response: Response<CocktailData>
            ) {
                cocktailItem = response.body()!!
                Log.d("dd", cocktailItem.toString())

                //#2. 화면 설정
                //2-1. 텍스트
                cocktailItemBinding.itemBarTitle.text = cocktailItem.title
                cocktailItemBinding.itemContentTitle.text = cocktailItem.title
                /*
                //이미지 출력
                recycleViewGradientAdapter = RecycleViewGradientAdapter(gradi)

                cocktailItemBinding.gradientImageList.adapter = recycleViewGradientAdapter
                cocktailItemBinding.gradientImageList.setPadding(100, 100, 100, 100)

                cocktailItemBinding.gradientImageList.offscreenPageLimit = 3
                cocktailItemBinding.gradientImageList.getChildAt(0).overScrollMode= View.OVER_SCROLL_NEVER

                var transform = CompositePageTransformer()
                transform.addTransformer(MarginPageTransformer(30))

                transform.addTransformer(ViewPager2.PageTransformer{ view: View, fl: Float ->
                    var v = 1-Math.abs(fl)
                    view.scaleY = 0.8f + v * 0.2f
                })

                cocktailItemBinding.gradientImageList.setPageTransformer(transform)*/

            }

            override fun onFailure(call: Call<CocktailData>, t: Throwable) {
                Toast.makeText(this@CocktailItemActivity, "칵테일 데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })*/
        /*
        //세부 아이템 데이터 세팅
        //값 받아오기 - 서버 X
        val title = intent.getStringExtra("title")
        val thumbnail = intent.getIntExtra("thumbnail", 0)
        val rating = intent.getFloatExtra("rating", 0f)
        val like = intent.getIntExtra("like", 0)
        val writtenDate = intent.getStringExtra("writtenDate")
        val alcoholLevel = intent.getStringExtra("alcoholLevel")
        val sweetLevel = intent.getStringExtra("sweetLevel")
        val gradientList = intent.getParcelableArrayListExtra<GradientData>("gradientList")
        val recipeContent = intent.getStringExtra("recipeContent")
        val balanceList = intent.getParcelableArrayListExtra<BalanceData>("balanceList")
        var reviewList = intent.getParcelableArrayListExtra<ReviewData>("reviewList")
        val position = intent.getIntExtra("position", 0)

        //값 적용하기
        cocktailItemBinding.itemBarTitle.text = title
        cocktailItemBinding.itemContentTitle.text = title
        cocktailItemBinding.pictureCocktail.setImageResource(thumbnail)
        cocktailItemBinding.ratingScore.text = rating.toString()
        cocktailItemBinding.countLike.text = like.toString()
        cocktailItemBinding.timeUpload.text = writtenDate
        cocktailItemBinding.aAlcohol.text = alcoholLevel
        cocktailItemBinding.aSweet.text = sweetLevel
        cocktailItemBinding.ratingCount.text = "(${reviewList!!.size})"
        cocktailItemBinding.avgRatingBar2.rating = rating
        cocktailItemBinding.ratingCount2.text = "(${reviewList!!.size})"
        cocktailItemBinding.ratingScore2.text = rating.toString()*/

        //수정된 내용 받아오기
        /*
        itemResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK) {
                //콘텐츠 내용 값 가져오기
                val reviewList2 = it.data?.getParcelableArrayListExtra<ReviewData>("reviewList")
                val total = it.data?.getFloatExtra("total", 3.2f)
                recyclerViewReviewAdapter = ReviewAdapter(reviewList2!!)
                val layoutManager2: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                cocktailItemBinding.listReview.layoutManager = layoutManager2
                cocktailItemBinding.listReview.adapter = recyclerViewReviewAdapter
                cocktailItemBinding.ratingScore.text = total.toString()
                cocktailItemBinding.ratingScore2.text = total.toString()
                cocktailItemBinding.ratingCount.text = "(${reviewList2.size})"
                cocktailItemBinding.ratingCount2.text = "(${reviewList2.size})"
                cocktailItemBinding.avgRatingBar2.rating = total!!.toFloat()

                recyclerViewReviewAdapter.notifyItemRangeChanged(reviewList.size, reviewList2.size - reviewList.size)
            }
        }

        //레시피
        //재료
        for (i in 0 until balanceList!!.size){
            materialNameList.add(balanceList[i].balanceName)
            materialNumList.add(balanceList[i].balanceNum)
        }

        for (i in 0 until gradientList!!.size) {
            materialNameList2.add(gradientList[i].gradientName)
        }

        cocktailItemBinding.materialCocktail.text = materialNameList2.joinToString(", ")
        cocktailItemBinding.ratioText.text = materialNameList.joinToString(" : ")
        cocktailItemBinding.ratioNum.text = materialNumList.joinToString (" : ")

        cocktailItemBinding.recipeContent.text = recipeContent

        //리뷰
        //리사이클러뷰 레이아웃 설정 + 어댑터 연결
        recyclerViewReviewAdapter = ReviewAdapter(reviewList!!)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        cocktailItemBinding.listReview.layoutManager = layoutManager
        cocktailItemBinding.listReview.adapter = recyclerViewReviewAdapter

        recyclerViewReviewAdapter.notifyItemRangeChanged(reviewList.size, reviewList.size)

        //리뷰 전체 보기 화면 불러오기
        cocktailItemBinding.reviewAll.setOnClickListener {
            val reviewIntent = Intent(this@CocktailItemActivity, ReviewActivity::class.java)
            reviewIntent.putParcelableArrayListExtra("reviewList", reviewList)
            itemResultLauncher.launch(reviewIntent)
        }
        
        //원래 화면으로 돌아가기
        cocktailItemBinding.buttonBack.setOnClickListener {
            val itemIntent = Intent(this@CocktailItemActivity, MainActivity::class.java)
            itemIntent.putExtra("title", title)
            itemIntent.putExtra("thumbnail", thumbnail)
            itemIntent.putExtra("rating", rating)
            itemIntent.putExtra("like", like)
            itemIntent.putExtra("position", position)

            //결과값 반환
            setResult(RESULT_OK, intent)

            //끝나지 않았다면
            if (!isFinishing) finish()
        }*/
    }
}