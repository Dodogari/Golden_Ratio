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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.goldenratio.databinding.FragmentHangoverBinding
import me.relex.circleindicator.CircleIndicator3
import org.json.JSONArray
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
    private var markList = arrayListOf<Boolean>()

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

        recyclerViewBoardAdapter = RecyclerViewBoardAdapter(hangoverList, markList)

        //리사이클러뷰 레이아웃 설정
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        hangoverBinding.listHangover.layoutManager = layoutManager
        hangoverBinding.listHangover.adapter = recyclerViewBoardAdapter

        //좋아요에 대한 sharedPreferences 객체 선언
        val likeSharedHangover = activity!!.getSharedPreferences("pref_hangover", AppCompatActivity.MODE_PRIVATE)
        val editor = likeSharedHangover.edit()
        val stringPref = likeSharedHangover.getString("key", "")

        //저장되어 있는 내용이 있다면
        if(stringPref != null && stringPref != "") {
            val arrJson = JSONArray(stringPref)

            //JSONArray 객체를 boolean 으로 전환 -> 배열에 저장
            for(i in 0 until arrJson.length())
                markList.add(arrJson.optBoolean(i))
        }

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
                recyclerViewBoardAdapter = RecyclerViewBoardAdapter(hangoverList, markList)
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
                        markList[position] = !markList[position]

                        if(markList[position] == true) {
                            hangoverList[position].likeCount++
                        }

                        else
                            hangoverList[position].likeCount--

                        //markList를 JSONArray 형식으로 변환
                        val jsonArr = JSONArray()
                        for (pos in markList)
                            jsonArr.put(pos)

                        //JSONArray를 문자열 형식으로 변환하여 sharedPreferences 객체에 저장한다.
                        val result = jsonArr.toString()

                        with(editor) {
                            putString("key", result)
                            apply()
                        }
                    }

                })
            }

            override fun onFailure(call: Call<ArrayList<BoardData>>, t: Throwable) {
                Toast.makeText(activity, "칵테일 데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })

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
    }

    //1-1. 슬라이드 이미지 추가
    private fun addSlideImage() {
        slideListHangover.add(R.drawable.viewpager_image1)
        slideListHangover.add(R.drawable.viewpager_image2)
        slideListHangover.add(R.drawable.viewpager_image3)
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
}