package com.example.goldenratio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.bumptech.glide.Glide
import com.example.goldenratio.cocktail.AddCocktailActivity
import com.example.goldenratio.databinding.ActivityCocktailItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

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

    @SuppressLint("DiscouragedPrivateApi")
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
        val cocktailItemContent = RegisterClient.cocktailService.getCocktailItem(boardId.toString())
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
                    cocktailItemBinding.countLike.text = cocktailItemData.likes.toString()
                    
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

                    try {
                        //2-8. 레시피명, 이미지
                        for (i in 0 until cocktailItemData.gradientList.size) {
                            cocktailRecipeName.add(cocktailItemData.gradientList[i].gradientName)
                            cocktailRecipeImage.add(cocktailItemData.gradientList[i].gradientImageUrl)
                        }
                        //레시피명
                        cocktailItemBinding.materialCocktail.text = cocktailRecipeName.joinToString(", ")
                        cocktailRecipeName.clear()

                        //레시피 이미지
                        //1) 어댑터 연결
                        recycleViewGradientAdapter = RecycleViewGradientAdapter(cocktailItemData.gradientList)
                        cocktailItemBinding.gradientImageList.adapter = recycleViewGradientAdapter

                        //2) 크기, 보여지는 아이템 갯수 정하기
                        cocktailItemBinding.gradientImageList.setPadding(150, 150, 150, 150)
                        cocktailItemBinding.gradientImageList.offscreenPageLimit = 3
                        cocktailItemBinding.gradientImageList.getChildAt(0).overScrollMode= View.OVER_SCROLL_NEVER

                        //3) 이미지 마진
                        val transform = CompositePageTransformer()
                        transform.addTransformer(MarginPageTransformer(100))

                        //4) 스크롤 시 이미지 크기 변동
                        transform.addTransformer { view: View, fl: Float ->
                            val v = 1 - abs(fl)
                            view.scaleY = 0.8f + v * 0.2f
                        }

                        cocktailItemBinding.gradientImageList.setPageTransformer(transform)
                    }

                    catch (e: NullPointerException) {
                        Toast.makeText(this@CocktailItemActivity, "재료 이미지 및 이름을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }

                    try {
                        //2-9. 레시피 비율
                        for (i in 0 until cocktailItemData.balanceList.size){
                            if(cocktailItemData.balanceList[i].balanceNum != 0){
                                cocktailRecipeName.add(cocktailItemData.balanceList[i].balanceName)
                                cocktailRecipeRatio.add(cocktailItemData.balanceList[i].balanceNum)
                            }
                        }

                        cocktailItemBinding.ratioText.text = cocktailRecipeName.joinToString(" : ")
                        cocktailItemBinding.ratioNum.text = cocktailRecipeRatio.joinToString(" : ")
                    }
                    catch (e: NullPointerException) {
                        Toast.makeText(this@CocktailItemActivity, "비율 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }

                    //2-10. 레시피 설명
                    cocktailItemBinding.recipeContent.text = cocktailItemData.content

                    //2-11. 리뷰 - 리사이클러뷰 레이아웃 설정 + 어댑터 연결
                    recyclerViewReviewAdapter = ReviewAdapter(cocktailItemData.reviews)

                    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@CocktailItemActivity, LinearLayoutManager.VERTICAL, false)
                    cocktailItemBinding.listReview.layoutManager = layoutManager
                    cocktailItemBinding.listReview.adapter = recyclerViewReviewAdapter

                    recyclerViewReviewAdapter.notifyItemRangeChanged(cocktailItemData.reviews.size, cocktailItemData.reviews.size)

                    //2-12. 날짜
                    cocktailItemBinding.timeUploadCocktail.text = "${cocktailItemData.createdDate[0]}년 ${cocktailItemData.createdDate[1]}월 ${cocktailItemData.createdDate[2]}일 작성"

                    //2-13. 메인 이미지
                    Glide.with(cocktailItemBinding.pictureCocktail)
                        .load(cocktailItemData.mainImage)
                        .into(cocktailItemBinding.pictureCocktail)
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

        //#4. 리뷰 전체 보기 화면 불러오기
        cocktailItemBinding.reviewAll.setOnClickListener {
            val reviewIntent = Intent(this@CocktailItemActivity, ReviewActivity::class.java)
            reviewIntent.putExtra("board", boardId)
            reviewIntent.putExtra("category", 0)
            startActivity(reviewIntent)
        }

        //#5. 팝업 메뉴
        //5-1. 메뉴 클릭 시 액션
        val popupMenu = PopupMenu(applicationContext, cocktailItemBinding.buttonMenu)
        popupMenu.inflate(R.menu.menu_option)
        popupMenu.setOnMenuItemClickListener{
            when(it.itemId) {
                R.id.edit_item -> {
                    val addCocktailIntent = Intent(this, AddCocktailActivity::class.java)
                    addCocktailIntent.putExtra("boardId", boardId)

                    startActivity(addCocktailIntent)
                    true
                }
                else -> true
            }
        }

        //5-1. 사이드 메뉴 버튼 클릭 시 메뉴 펼침
        cocktailItemBinding.buttonMenu.setOnClickListener {
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenu)

            menu.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)

            popupMenu.show()
        }
    }
}