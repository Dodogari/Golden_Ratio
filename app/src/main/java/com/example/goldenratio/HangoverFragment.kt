package com.example.goldenratio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.goldenratio.databinding.FragmentHangoverBinding
import me.relex.circleindicator.CircleIndicator3
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HangoverFragment : Fragment() {
    private lateinit var hangoverBinding: FragmentHangoverBinding

    //#1. 상단 슬라이드 배너 변수
    private var slideListHangover: MutableList<Int> = mutableListOf()                       //슬라이드에 보여질 이미지 리스트
    private lateinit var topSlideBannerViewPager2: ViewPager2                       //슬라이드 뷰페이저 리스트
    private lateinit var indicator3: CircleIndicator3                               //슬라이드 인디케이터
    private lateinit var handler: Handler                                           //핸들러 (메시지 전달)

    //#2. 숙취해소 리스트 변수
    private var recyclerViewBoardAdapter: RecyclerViewBoardAdapter? = null          //숙취해소 리사이클러뷰 리스트 어댑터
    private var hangoverList = arrayListOf<BoardData>()                             //숙취해소 데이터 리스트

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    //레이아웃 inflate(객체화)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //바인딩 정의
        hangoverBinding = FragmentHangoverBinding.inflate(inflater, container, false)
        return hangoverBinding.root
    }

    //초기값 설정
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //#1. 상단 슬라이드 설정
        //1-1. 슬라이드 이미지 추가
        addSlideImage()
        handler = Handler(Looper.myLooper()!!)  //핸들러 설정

        //1-2. viewPager2 설정
        topSlideBannerViewPager2 = hangoverBinding.slideViewPagerHangover
        topSlideBannerViewPager2.adapter = TopSlideBannerAdapter(slideListHangover)

        //1-3. indicator 연결
        indicator3 = hangoverBinding.topSlideBannerIndicatorHangover
        indicator3.setViewPager(topSlideBannerViewPager2)

        //1-4. 자동 슬라이드 설정
        topSlideBannerViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    //스크롤 상태일 때 범위를 초과한다면
                    when (topSlideBannerViewPager2.currentItem) {
                        //오른쪽: 맨 첫번째 사진 뷰를 보여줌
                        (slideListHangover.size + 1) -> topSlideBannerViewPager2.setCurrentItem(0, false)
                        //왼쪽: 맨 마지막 사진 뷰를 보여줌
                        -1 -> topSlideBannerViewPager2.setCurrentItem(slideListHangover.size, false)
                    }
                }
            }
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indicator3.animatePageSelected(position)        //인디케이터와 뷰페이저 연결

                handler.removeCallbacks(runnable)               //슬라이드 멈춤
                handler.postDelayed(runnable, 3000)    //3초에 한번 슬라이드
            }
        })


        recyclerViewBoardAdapter = RecyclerViewBoardAdapter(hangoverList)

        //리사이클러뷰 레이아웃 설정
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        hangoverBinding.listHangover.layoutManager = layoutManager
        hangoverBinding.listHangover.adapter = recyclerViewBoardAdapter



        //#2. 서버 통신: 숙취해소 보드 내용 받아오기
        //2-1. 응답
        var HangoverListContent = RegisterClient.hangoverService.getHangoverListAll()
        HangoverListContent.enqueue(object : Callback<ArrayList<BoardData>> {
            //서버 응답 시
            override fun onResponse(
                call: Call<ArrayList<BoardData>>,
                response: Response<ArrayList<BoardData>>
            ) {
                hangoverList = response.body()!!
                Log.d("왜 안돼", hangoverList.toString())

                //#3. 리사이클러뷰 설정
                //3-1. 리사이클러뷰 레이아웃 설정
                hangoverBinding.listHangover.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)

                //3-2. 어댑터 - 리스트 연결
                recyclerViewBoardAdapter = RecyclerViewBoardAdapter(hangoverList)
                hangoverBinding.listHangover.adapter = recyclerViewBoardAdapter

                //#5. 아이템 클릭 시
                recyclerViewBoardAdapter!!.setOnClickListener(object : RecyclerViewBoardAdapter.OnClickListener {
                    //5-1. 상세 메뉴 액티비티로 전환
                    override fun onClick(position: Int) {
                        val itemIntent = Intent(activity, HangoverItemActivity::class.java)
                        itemIntent.putExtra("boardId", hangoverList[position].boardId)
                        startActivity(itemIntent)
                    }

                    //5-2. 좋아요 클릭
                    override fun likeOnClick(position: Int) {
                    }

                })
            }

            override fun onFailure(call: Call<ArrayList<BoardData>>, t: Throwable) {
                Toast.makeText(activity, "칵테일 데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        /*
        resultLauncher = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == android.app.Activity.RESULT_OK) {
                //콘텐츠 내용 값 가져오기
                val title = it.data?.getStringExtra("title")
                val rating = it.data?.getFloatExtra("rating", 0f)
                val thumbnail = it.data?.getIntExtra("thumbnail", 0)
                val like = it.data?.getIntExtra("like", 0)
                val position = it.data?.getIntExtra("position", 0)

                hangoverList[position!!].title = title.toString()
                hangoverList[position].thumbnail = thumbnail!!.toInt()
                hangoverList[position].like = like!!.toInt()
                hangoverList[position].rating = rating!!.toFloat()
            }
        }*/

        //#4. 라디오 버튼 클릭
        hangoverBinding.radioHangoverAll.isChecked = true

        //4-1. 전체
        hangoverBinding.radioHangoverAll.setOnClickListener {
            HangoverListContent = RegisterClient.hangoverService.getHangoverListAll()
            HangoverListContent.enqueue(object : Callback<ArrayList<BoardData>> {
                //서버 응답 시
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ArrayList<BoardData>>,
                    response: Response<ArrayList<BoardData>>
                ) {
                    hangoverList = response.body()!!
                    recyclerViewBoardAdapter!!.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<ArrayList<BoardData>>, t: Throwable) {
                    Toast.makeText(activity, "칵테일 데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        //4-2. 별점순
        hangoverBinding.radioHangoverRating.setOnClickListener {
            HangoverListContent = RegisterClient.hangoverService.getHangoverListStar()
            HangoverListContent.enqueue(object : Callback<ArrayList<BoardData>> {
                //서버 응답 시
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ArrayList<BoardData>>,
                    response: Response<ArrayList<BoardData>>
                ) {
                    hangoverList = response.body()!!
                    recyclerViewBoardAdapter!!.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<ArrayList<BoardData>>, t: Throwable) {
                    Toast.makeText(activity, "칵테일 데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        //4-3. 좋아요순
        hangoverBinding.radioHangoverLike.setOnClickListener {
            HangoverListContent = RegisterClient.hangoverService.getHangoverListLike()
            HangoverListContent.enqueue(object : Callback<ArrayList<BoardData>> {
                //서버 응답 시
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ArrayList<BoardData>>,
                    response: Response<ArrayList<BoardData>>
                ) {
                    hangoverList = response.body()!!
                    recyclerViewBoardAdapter!!.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<ArrayList<BoardData>>, t: Throwable) {
                    Toast.makeText(activity, "칵테일 데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        //아이템 클릭 시
        //숙취해소 리스트 아이템 클릭 시
        recyclerViewBoardAdapter!!.setOnClickListener(object : RecyclerViewBoardAdapter.OnClickListener {
            //상세 페이지 불러오기
            override fun onClick(position: Int) {
                /*
                val itemIntent = Intent(activity, HangoverItemActivity::class.java)
                itemIntent.putExtra("title", hangoverList[position].title)
                itemIntent.putExtra("thumbnail", hangoverList[position].thumbnail)
                itemIntent.putExtra("like", hangoverList[position].like)
                itemIntent.putExtra("writtenDate", hangoverList[position].writtenDate)
                itemIntent.putExtra("position", position)                   //배열 위치 넘기기
                itemIntent.putParcelableArrayListExtra("gradientList", hangoverList[position].gradientList)
                itemIntent.putExtra("recipeContent", hangoverList[position].recipeContent)
                itemIntent.putParcelableArrayListExtra("reviewList", hangoverList[position].reviewList)
                itemIntent.putExtra("position", position)                   //배열 위치 넘기기

                resultLauncher.launch(itemIntent)*/
            }

            override fun likeOnClick(position: Int) {
                //hangoverList[position].likeCheck = !hangoverList[position].likeCheck
            }

        })
    }

    //1-1. 슬라이드 이미지 추가
    private fun addSlideImage() {
        slideListHangover.add(R.drawable.view_test1)
        slideListHangover.add(R.drawable.view_test2)
        slideListHangover.add(R.drawable.view_test3)
    }

    //1-4. runnable 객체 정의
    private val runnable = Runnable {
        //맨 마지막 이미지에 도달하면 처음 이미지로 되돌아감
        if (topSlideBannerViewPager2.currentItem == slideListHangover.size - 1)
            topSlideBannerViewPager2.currentItem = 0
        //그 외엔 마지막으로 향해 이미지 슬라이드
        else
            topSlideBannerViewPager2.currentItem = topSlideBannerViewPager2.currentItem + 1
    }

    //Resume 상태라면 현재 페이지에서 3초 대기
    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 3000)
    }
/*
    private fun testHangoverList() {
        hangoverList.add(HangoverData("숙취해소 1", R.drawable.egg, 4.5f, 6, likeCheck = false,
            "2023년 7월 20일 작성", arrayListOf<GradientData>(GradientData("소주", R.drawable.egg),
                GradientData("토닉워터", R.drawable.cell), GradientData("스프라이트", R.drawable.chicken)),
            "소주와 깔라만시 맛 토닉워터를 같은 비율로 따라준 후, 하리보 2~3개를 넣어주면 달달한 소토닉 완성", arrayListOf(ReviewData("유저 2", R.drawable.drop, 3, "뭔가 아쉬운 맛?"), ReviewData("유저 38", R.drawable.age, 2, "애매..."),
                ReviewData("유저 47", R.drawable.chicken, 4, "나름 괜춘"), ReviewData("유저 4", R.drawable.cry, 5, "딜리셔스"),
                ReviewData("유저 12", R.drawable.age, 2, "잡내 난다"), ReviewData("유저 38", R.drawable.egg, 3, "남이 해주면 먹음"))))

        hangoverList.add(HangoverData("숙취해소 2", R.drawable.drop, 2.8f, 3, likeCheck = false,
            "2023년 7월 20일 작성", arrayListOf<GradientData>(GradientData("소주", R.drawable.egg),
                GradientData("헛개수", R.drawable.age), GradientData("솔의눈", R.drawable.cry), GradientData("데자와", R.drawable.cell)),
            "건강에 좋을 것 같은 메뉴",  arrayListOf<ReviewData>(ReviewData("유저 15", R.drawable.drop, 3, "쏘쏘"),
                ReviewData("유저 48", R.drawable.age, 2, "애매..."), ReviewData("유저 76", R.drawable.chicken, 3, "50프로 부족한 맛"),
                ReviewData("유저 45", R.drawable.cry, 4, "좀 더..."), ReviewData("유저 57", R.drawable.age, 1, "혼종"),
                ReviewData("유저 80", R.drawable.egg, 2, "아쉽다"))))

        hangoverList.add(HangoverData("숙취해소 3", R.drawable.age, 3.2f, 20, likeCheck = false,
            "2023년 7월 20일 작성", arrayListOf<GradientData>(GradientData("소주", R.drawable.egg),
                GradientData("여명808", R.drawable.cry), GradientData("스프라이트", R.drawable.chicken)),
            "소주와 깔라만시 맛 토닉워터를 같은 비율로 따라준 후, 하리보 2~3개를 넣어주면 달달한 소토닉 완성",
            arrayListOf<ReviewData>(ReviewData("유저 21", R.drawable.drop, 3, "쏘쏘"),
                ReviewData("유저 29", R.drawable.age, 2, "애매..."), ReviewData("유저 40", R.drawable.cell, 4, "가끔씩 해먹을 것 같다"),
                ReviewData("유저 3", R.drawable.drop, 5, "매우 추천"), ReviewData("유저 7", R.drawable.egg, 2, "이러는 이유가 있을 거 아니예요"))))

        hangoverList.add(HangoverData("숙취해소 4", R.drawable.cell, 4.5f, 3, likeCheck = false,
            "2023년 7월 20일 작성", arrayListOf<GradientData>(GradientData("소주", R.drawable.egg),
                GradientData("토닉워터", R.drawable.cell), GradientData("스프라이트", R.drawable.chicken)),
            "소주와 깔라만시 맛 토닉워터를 같은 비율로 따라준 후, 하리보 2~3개를 넣어주면 달달한 소토닉 완성",  arrayListOf<ReviewData>(ReviewData("유저 15", R.drawable.drop, 3, "쏘쏘"),
                ReviewData("유저 48", R.drawable.age, 2, "애매..."), ReviewData("유저 76", R.drawable.chicken, 3, "50프로 부족한 맛"),
                ReviewData("유저 45", R.drawable.cry, 4, "좀 더..."), ReviewData("유저 57", R.drawable.age, 1, "혼종"),
                ReviewData("유저 80", R.drawable.egg, 2, "아쉽다"))))

        hangoverList.add(HangoverData("숙취해소 5", R.drawable.chicken, 2.3f, 2, likeCheck = false,
            "2023년 7월 20일 작성", arrayListOf<GradientData>(GradientData("소주", R.drawable.egg),
                GradientData("토닉워터", R.drawable.cell), GradientData("스프라이트", R.drawable.chicken)),
            "소주와 깔라만시 맛 토닉워터를 같은 비율로 따라준 후, 하리보 2~3개를 넣어주면 달달한 소토닉 완성", arrayListOf(ReviewData("유저 5", R.drawable.drop, 4, "맛남"), ReviewData("유저 8", R.drawable.age, 2, "애매..."),
                ReviewData("유저 7", R.drawable.chicken, 3, "무난함"), ReviewData("유저 1", R.drawable.cry, 5, "내 취향"),
                ReviewData("유저 32", R.drawable.age, 1, "미각을 잃었다"), ReviewData("유저 17", R.drawable.egg, 2, "때깔이..."))))

        hangoverList.add(HangoverData("숙취해소 6", R.drawable.cry, 1.6f, 3, likeCheck = false,
            "2023년 7월 20일 작성", arrayListOf<GradientData>(GradientData("소주", R.drawable.egg),
                GradientData("토닉워터", R.drawable.cell), GradientData("스프라이트", R.drawable.chicken)),
            "소주와 깔라만시 맛 토닉워터를 같은 비율로 따라준 후, 하리보 2~3개를 넣어주면 달달한 소토닉 완성", arrayListOf(ReviewData("유저 2", R.drawable.drop, 3, "뭔가 아쉬운 맛?"), ReviewData("유저 38", R.drawable.age, 2, "애매..."),
                ReviewData("유저 47", R.drawable.chicken, 4, "나름 괜춘"), ReviewData("유저 4", R.drawable.cry, 5, "딜리셔스"),
                ReviewData("유저 12", R.drawable.age, 2, "잡내 난다"), ReviewData("유저 38", R.drawable.egg, 3, "남이 해주면 먹음"))))

        recyclerViewHangoverAdapter!!.notifyItemRangeChanged(hangoverList.size, 6)
    }*/
}