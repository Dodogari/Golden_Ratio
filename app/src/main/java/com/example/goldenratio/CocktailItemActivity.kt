package com.example.goldenratio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goldenratio.databinding.ActivityCocktailItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CocktailItemActivity : AppCompatActivity() {
    private lateinit var cocktailItemBinding: ActivityCocktailItemBinding
    //#1. 서버 통신 - 서버 데이터 response 값
    private lateinit var cocktailItemData: CocktailData

    //#2. 화면 초기화
    //2-1. 레시피명 리스트
    private var cocktailRecipeName = arrayListOf<String>()
    private var cocktailRecipeImage = arrayListOf<String>()

    //2-2. 레시피 비율 리스트
    private var cocktailRecipeRatio = arrayListOf<Int>()

    //2-3. 레시피 이미지 리스트
    private lateinit var recycleViewGradientAdapter: RecycleViewGradientAdapter

    //2-4. 리뷰 리스트 어댑터
    private lateinit var recyclerViewReviewAdapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding 정의
        cocktailItemBinding = ActivityCocktailItemBinding.inflate(layoutInflater)
        setContentView(cocktailItemBinding.root)

        //사이드 메뉴
        setSupportActionBar(cocktailItemBinding.topBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.button_menu)

        //#1. 서버 통신: 세부 칵테일 보드 내용 받아오기
        //1-1. 데이터 포지션
        val boardId = intent.getIntExtra("boardId", 1).toString()
        Log.d("dd", boardId)

        //1-2. 통신
        val cocktailItemContent = RegisterClient.registerService.getCocktailItem(boardId)
        cocktailItemContent.enqueue(object : Callback<CocktailData> {
            //서버 응답 시
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<CocktailData>,
                response: Response<CocktailData>) {

                //통신 성공 시
                if(response.isSuccessful){
                    cocktailItemData = response.body()!!

                    //#2. 화면 초기화
                    //2-1. 칵테일 제목
                    cocktailItemBinding.itemContentTitle.text = cocktailItemData.title
                    cocktailItemBinding.itemBarTitle.text = cocktailItemData.title

                    //2-2. 리뷰 갯수
                    cocktailItemBinding.ratingCount.text = "(${cocktailItemData.reviews.size})"
                    cocktailItemBinding.ratingCount2.text = "(${cocktailItemData.reviews.size})"

                    //2-3. 별점 - 텍스트
                    cocktailItemBinding.ratingScore.text = cocktailItemData.averageScore.toString()
                    cocktailItemBinding.ratingScore2.text = cocktailItemData.averageScore.toString()

                    //2-4. 별점 - 레이팅바
                    cocktailItemBinding.avgRatingBar2.rating = cocktailItemData.averageScore

                    //2-5. 좋아요 갯수
                    cocktailItemBinding.countLike.text = "(${cocktailItemData.likes})"
                    
                    //2-6. 단맛 -> 숫자에 따라 표기
                    when(cocktailItemData.sweet) {
                        0 -> cocktailItemBinding.aSweet.text = "상"
                        1 -> cocktailItemBinding.aSweet.text = "중"
                        2 -> cocktailItemBinding.aSweet.text = "하"
                    }

                    //2-7. 도수 -> 숫자에 따라 표기
                    when(cocktailItemData.alcohol) {
                        0 -> cocktailItemBinding.aAlcohol.text = "소주보다 낮음"
                        1 -> cocktailItemBinding.aAlcohol.text = "소주"
                        2 -> cocktailItemBinding.aAlcohol.text = "소주보다 높음"
                    }

                    //2-8. 레시피명, 이미지
                    for (i in 0 until cocktailItemData.gradientList.size) {
                        cocktailRecipeName.add(cocktailItemData.gradientList[i].gradientName)
                        cocktailRecipeImage.add(cocktailItemData.gradientList[i].gradientImageUrl)
                    }
                    //레시피명
                    cocktailItemBinding.materialCocktail.text = cocktailRecipeName.joinToString(", ")
                    cocktailRecipeName.clear()

                    //레시피 이미지
                    //이미지 출력
                    /*
                    recycleViewGradientAdapter = RecycleViewGradientAdapter(cocktailRecipeImage)

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

                    //2-9. 레시피 비율
                    for (i in 0 until cocktailItemData.balanceList.size){
                        if(cocktailItemData.balanceList[i].balanceNum != 0){
                            cocktailRecipeName.add(cocktailItemData.balanceList[i].balanceName)
                            cocktailRecipeRatio.add(cocktailItemData.balanceList[i].balanceNum)
                        }
                    }
                    cocktailItemBinding.ratioText.text = cocktailRecipeName.joinToString(" : ")
                    cocktailItemBinding.ratioNum.text = cocktailRecipeRatio.joinToString(" : ")

                    //2-10. 레시피 설명
                    cocktailItemBinding.recipeContent.text = cocktailItemData.content

                    //2-11. 리뷰
                    //1) 리사이클러뷰 레이아웃 설정 + 어댑터 연결
                    recyclerViewReviewAdapter = ReviewAdapter(cocktailItemData.reviews)

                    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@CocktailItemActivity, LinearLayoutManager.VERTICAL, false)
                    cocktailItemBinding.listReview.layoutManager = layoutManager
                    cocktailItemBinding.listReview.adapter = recyclerViewReviewAdapter

                    recyclerViewReviewAdapter.notifyItemRangeChanged(cocktailItemData.reviews.size, cocktailItemData.reviews.size)

                    //2) 리뷰 전체 보기 화면 불러오기
                    cocktailItemBinding.reviewAll.setOnClickListener {
                        startActivity(Intent(this@CocktailItemActivity, ReviewActivity::class.java))
                    }

                    //2-12. 날짜
                    cocktailItemBinding.timeUpload.text = "${cocktailItemData.createdDate[0]}년 ${cocktailItemData.createdDate[1]}월 ${cocktailItemData.createdDate[2]}일 작성"
                }
                else
                    Log.d("error", response.body().toString())
            }

            override fun onFailure(call: Call<CocktailData>, t: Throwable) {
                Toast.makeText(this@CocktailItemActivity, "칵테일 데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        //#3. back 버튼 -> 메인 화면으로 돌아가기
        cocktailItemBinding.buttonBack.setOnClickListener {
            startActivity(Intent(this@CocktailItemActivity, MainActivity::class.java))
            if (!isFinishing) finish()
        }

        /*val cocktailItemContent = RegisterClient.registerService.getCocktailItem(boardId,)
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