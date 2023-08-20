package com.example.goldenratio

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.goldenratio.databinding.ActivityCocktailItemBinding
import com.example.goldenratio.databinding.ActivityHangoverItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

class HangoverItemActivity : AppCompatActivity() {
    private lateinit var hangoverItemBinding: ActivityHangoverItemBinding
    //#1. 서버 통신 - 서버 데이터 response 값
    private lateinit var hangoverItemData: HangoverData

    //#2. 화면 초기화
    //2-1. 레시피명 리스트
    private var hangoverRecipeName = arrayListOf<String>()
    private var hangoverRecipeImage = arrayListOf<String>()

    //2-3. 레시피 이미지 리스트
    private lateinit var recycleViewGradientAdapter: RecycleViewGradientAdapter

    //2-4. 리뷰 리스트 어댑터
    private lateinit var recyclerViewReviewAdapter: ReviewAdapter

    @SuppressLint("DiscouragedPrivateApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding 정의
        hangoverItemBinding = ActivityHangoverItemBinding.inflate(layoutInflater)
        setContentView(hangoverItemBinding.root)

        //#1. 서버 통신: 세부 숙취해소 보드 내용 받아오기
        //1-1. 데이터 포지션
        val boardId = intent.getIntExtra("boardId", 1).toString()
        Log.d("dd", boardId)

        //1-2. 통신
        val hangoverItemContent = RegisterClient.hangoverService.getHangoverItem(boardId)
        hangoverItemContent.enqueue(object : Callback<HangoverData> {
            //서버 응답 시
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<HangoverData>,
                response: Response<HangoverData>
            ) {
                //통신 성공 시
                if(response.isSuccessful){
                    hangoverItemData = response.body()!!

                    //#2. 화면 초기화
                    //2-1. 칵테일 제목
                    hangoverItemBinding.itemContentTitleHangover.text = hangoverItemData.title
                    hangoverItemBinding.itemBarTitleHangover.text = hangoverItemData.title

                    //2-2. 리뷰 갯수
                    hangoverItemBinding.counts.text = "(${hangoverItemData.reviews.size})"
                    hangoverItemBinding.countsReview.text = "(${hangoverItemData.reviews.size})"

                    //2-3. 별점 - 텍스트
                    hangoverItemBinding.score.text = hangoverItemData.averageScore.toString()
                    hangoverItemBinding.scoreReview.text = hangoverItemData.averageScore.toString()

                    //2-4. 별점 - 레이팅바
                    hangoverItemBinding.ratingBarHangover.rating = hangoverItemData.averageScore

                    //2-5. 좋아요 갯수
                    hangoverItemBinding.likes.text = hangoverItemData.likes.toString()

                    //2-8. 레시피명, 이미지
                    for (i in 0 until hangoverItemData.gradientList.size) {
                        hangoverRecipeName.add(hangoverItemData.gradientList[i].gradientName)
                        hangoverRecipeImage.add(hangoverItemData.gradientList[i].gradientImageUrl)
                    }

                    //레시피명
                    hangoverItemBinding.materialHangover.text = hangoverRecipeName.joinToString(", ")

                    //레시피 이미지
                    //1) 어댑터 연결
                    recycleViewGradientAdapter = RecycleViewGradientAdapter(hangoverItemData.gradientList)
                    hangoverItemBinding.gradientImageListHangover.adapter = recycleViewGradientAdapter

                    //2) 크기, 보여지는 아이템 갯수 정하기
                    hangoverItemBinding.gradientImageListHangover.setPadding(150, 150, 150, 150)
                    hangoverItemBinding.gradientImageListHangover.offscreenPageLimit = 3
                    hangoverItemBinding.gradientImageListHangover.getChildAt(0).overScrollMode= View.OVER_SCROLL_NEVER

                    //3) 이미지 마진
                    val transform = CompositePageTransformer()
                    transform.addTransformer(MarginPageTransformer(100))

                    //4) 스크롤 시 이미지 크기 변동
                    transform.addTransformer { view: View, fl: Float ->
                        val v = 1 - abs(fl)
                        view.scaleY = 0.8f + v * 0.2f
                    }

                    hangoverItemBinding.gradientImageListHangover.setPageTransformer(transform)

                    //2-10. 레시피 설명
                    hangoverItemBinding.recipeContentHangover.text = hangoverItemData.content

                    //2-11. 리뷰 - 리사이클러뷰 레이아웃 설정 + 어댑터 연결
                    recyclerViewReviewAdapter = ReviewAdapter(hangoverItemData.reviews)

                    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@HangoverItemActivity, LinearLayoutManager.VERTICAL, false)
                    hangoverItemBinding.listReviewHangover.layoutManager = layoutManager
                    hangoverItemBinding.listReviewHangover.adapter = recyclerViewReviewAdapter

                    recyclerViewReviewAdapter.notifyItemRangeChanged(hangoverItemData.reviews.size, hangoverItemData.reviews.size)

                    //2-12. 날짜
                    hangoverItemBinding.timeUploadHangover.text = "${hangoverItemData.createdDate[0]}년 ${hangoverItemData.createdDate[1]}월 ${hangoverItemData.createdDate[2]}일 작성"

                    //2-13. 메인 이미지
                    Glide.with(hangoverItemBinding.mainImageHangover)
                        .load(hangoverItemData.mainImage)
                        .into(hangoverItemBinding.mainImageHangover)
                }
                else
                    Log.d("error", response.body().toString())
            }

            override fun onFailure(call: Call<HangoverData>, t: Throwable) {
                Toast.makeText(this@HangoverItemActivity, "칵테일 데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        //#3. back 버튼 -> 메인 화면으로 돌아가기
        hangoverItemBinding.buttonBackHangover.setOnClickListener {
            startActivity(Intent(this@HangoverItemActivity, MainActivity::class.java))
            if (!isFinishing) finish()
        }

        //#4. 리뷰 전체 보기 화면 불러오기
        hangoverItemBinding.reviewAll.setOnClickListener {
            val reviewIntent = Intent(this@HangoverItemActivity, ReviewActivity::class.java)
            reviewIntent.putExtra("boardId", boardId)
            startActivity(reviewIntent)
        }

        //#5. 팝업 메뉴
        //5-1. 메뉴 클릭 시 액션
        val popupMenu = PopupMenu(applicationContext, hangoverItemBinding.buttonMenu)
        popupMenu.inflate(R.menu.menu_option)
        popupMenu.setOnMenuItemClickListener{
            when(it.itemId) {
                R.id.edit_item -> {
                    Toast.makeText(this, "수정하기", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.delete_item -> {
                    Toast.makeText(this, "삭제하기", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> true
            }
        }

        //5-1. 사이드 메뉴 버튼 클릭 시 메뉴 펼침
        hangoverItemBinding.buttonMenu.setOnClickListener {
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