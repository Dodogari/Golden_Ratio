package com.example.goldenratio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.config.ApplicationClass
import com.example.config.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.example.config.ApplicationClass.Companion.sSharedPreferences
import com.example.goldenratio.databinding.FragmentCocktailBinding
import me.relex.circleindicator.CircleIndicator3
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CocktailFragment : Fragment() {
    private lateinit var cocktailBinding: FragmentCocktailBinding

    //#1. 상단 슬라이드 배너 변수
    private var slideList: MutableList<Int> = mutableListOf()                       //슬라이드에 보여질 이미지 리스트
    private lateinit var topSlideBannerViewPager2: ViewPager2                       //슬라이드 뷰페이저 리스트
    private lateinit var topSlideBannerAdapter: TopSlideBannerAdapter               //슬라이드 뷰페이저 어댑터
    private lateinit var handler: Handler                                           //핸들러 (메시지 전달)

    //#2. 칵테일 리스트 변수
    private var recyclerViewBoardAdapter: RecyclerViewBoardAdapter? = null    //칵테일 리사이클러뷰 리스트 어댑터
    private var cocktailList = arrayListOf<BoardData>()
    private var markList = arrayListOf<Boolean>()

    //레이아웃 inflate(객체화)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //바인딩 정의
        cocktailBinding = FragmentCocktailBinding.inflate(inflater, container, false)
        return cocktailBinding.root
    }

    //초기값 설정
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //#1. 상단 슬라이드 설정
        //1-1. 슬라이드 이미지 추가
        addSlideImage()
        handler = Handler(Looper.myLooper()!!)  //핸들러 설정

        //1-2. viewPager2 설정
        topSlideBannerViewPager2 = cocktailBinding.slideViewPager
        topSlideBannerAdapter = TopSlideBannerAdapter(slideList)
        topSlideBannerViewPager2.adapter = topSlideBannerAdapter

        //1-3. indicator 연결
        cocktailBinding.topSlideBannerIndicator.setViewPager(topSlideBannerViewPager2)

        //1-4. 자동 슬라이드 설정
        topSlideBannerViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    //스크롤 상태일 때 범위를 초과한다면
                    when (topSlideBannerViewPager2.currentItem) {
                        //오른쪽: 맨 첫번째 사진 뷰를 보여줌
                        (slideList.size + 1) -> topSlideBannerViewPager2.setCurrentItem(0, false)
                        //왼쪽: 맨 마지막 사진 뷰를 보여줌
                        -1 -> topSlideBannerViewPager2.setCurrentItem(slideList.size, false)
                    }
                }
            }
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                cocktailBinding.topSlideBannerIndicator.animatePageSelected(position)        //인디케이터와 뷰페이저 연결

                handler.removeCallbacks(runnable)               //슬라이드 멈춤
                handler.postDelayed(runnable, 3000)    //3초에 한번 슬라이드
            }
        })
        //좋아요에 대한 sharedPreferences 객체 선언
        val likeShared = activity!!.getSharedPreferences("pref", AppCompatActivity.MODE_PRIVATE)
        val editor = likeShared.edit()
        val stringPref = likeShared.getString("key", "")

        //저장되어 있는 내용이 있다면
        if(stringPref != null && stringPref != "") {
            val arrJson = JSONArray(stringPref)

            //JSONArray 객체를 boolean 으로 전환 -> 배열에 저장
            for(i in 0 until arrJson.length())
                markList.add(arrJson.optBoolean(i))
        }

        //#2. 서버 통신: 칵테일 보드 내용 받아오기
        //2-1. 응답
        var cocktailListContent = RegisterClient.cocktailService.getCocktailAll()
        cocktailListContent.enqueue(object : Callback<ArrayList<BoardData>> {
            //서버 응답 시
            override fun onResponse(
                call: Call<ArrayList<BoardData>>,
                response: Response<ArrayList<BoardData>>) {
                cocktailList = response.body()!!

                //#3. 리사이클러뷰 설정
                //3-1. 리사이클러뷰 레이아웃 설정
                cocktailBinding.listCocktail.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)

                //3-2. 어댑터 - 리스트 연결
                recyclerViewBoardAdapter = RecyclerViewBoardAdapter(cocktailList, markList)
                cocktailBinding.listCocktail.adapter = recyclerViewBoardAdapter

                recyclerViewBoardAdapter!!.setOnClickListener(object : RecyclerViewBoardAdapter.OnClickListener {
                    //5-1. 상세 메뉴 액티비티로 전환`

                    override fun onClick(position: Int) {
                        val itemIntent = Intent(activity, CocktailItemActivity::class.java)
                        itemIntent.putExtra("boardId", cocktailList[position].boardId)
                        startActivity(itemIntent)
                    }

                    //5-2. 좋아요 클릭
                    override fun likeOnClick(position: Int) {
                        val likeContent = RegisterClient.cocktailService.registerLikes(cocktailList[position].boardId.toString(), X_ACCESS_TOKEN)
                        likeContent.enqueue(object : Callback<PostResponse> {
                            override fun onResponse(
                                call: Call<PostResponse>,
                                response: Response<PostResponse>
                            ) {
                                val resultPost = response.body()!!.result
                                Toast.makeText(context, resultPost, Toast.LENGTH_SHORT).show()
                                markList[position] = !markList[position]

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

                            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                                Toast.makeText(context, "좋아요 등록을 실패하였습니다.", Toast.LENGTH_SHORT).show()
                            }

                        })
                    }
                })
                response.errorBody().toString()
            }

            override fun onFailure(call: Call<ArrayList<BoardData>>, t: Throwable) {
                Toast.makeText(activity, "칵테일 데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        //#4. 라디오 버튼 클릭
        cocktailBinding.radioCocktailAll.isChecked = true

        //4-1. 전체
        cocktailBinding.radioCocktailAll.setOnClickListener {
            cocktailListContent = RegisterClient.cocktailService.getCocktailAll()
            cocktailListContent.enqueue(object : Callback<ArrayList<BoardData>> {
                //서버 응답 시
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ArrayList<BoardData>>,
                    response: Response<ArrayList<BoardData>>) {
                    cocktailList = response.body()!!

                    recyclerViewBoardAdapter!!.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<ArrayList<BoardData>>, t: Throwable) {
                    Toast.makeText(activity, "데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        //4-2. 별점순
        cocktailBinding.radioCocktailRating.setOnClickListener {

            cocktailListContent = RegisterClient.cocktailService.getCocktailStar()
            cocktailListContent.enqueue(object : Callback<ArrayList<BoardData>> {
                //서버 응답 시
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ArrayList<BoardData>>,
                    response: Response<ArrayList<BoardData>>) {
                    cocktailList = response.body()!!

                    recyclerViewBoardAdapter!!.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<ArrayList<BoardData>>, t: Throwable) {
                    Toast.makeText(activity, "데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        //4-3. 좋아요순
        cocktailBinding.radioCocktailLike.setOnClickListener {

            cocktailListContent = RegisterClient.cocktailService.getCocktailLike()
            cocktailListContent.enqueue(object : Callback<ArrayList<BoardData>> {
                //서버 응답 시
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ArrayList<BoardData>>,
                    response: Response<ArrayList<BoardData>>) {
                    cocktailList = response.body()!!

                    recyclerViewBoardAdapter!!.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<ArrayList<BoardData>>, t: Throwable) {
                    Toast.makeText(activity, "데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        //4-4. 도수 높은 순
        cocktailBinding.radioCocktailAlcohol.setOnClickListener {

            cocktailListContent = RegisterClient.cocktailService.getCocktailAlcohol()
            cocktailListContent.enqueue(object : Callback<ArrayList<BoardData>> {
                //서버 응답 시
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ArrayList<BoardData>>,
                    response: Response<ArrayList<BoardData>>) {
                    cocktailList = response.body()!!

                    recyclerViewBoardAdapter!!.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<ArrayList<BoardData>>, t: Throwable) {
                    Toast.makeText(activity, "데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        //4-5. 단맛높은순
        cocktailBinding.radioCocktailSweet.setOnClickListener {

            cocktailListContent = RegisterClient.cocktailService.getCocktailSweet()
            cocktailListContent.enqueue(object : Callback<ArrayList<BoardData>> {
                //서버 응답 시
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ArrayList<BoardData>>,
                    response: Response<ArrayList<BoardData>>) {
                    cocktailList = response.body()!!

                    recyclerViewBoardAdapter!!.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<ArrayList<BoardData>>, t: Throwable) {
                    Toast.makeText(activity, "데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    //1-1. 슬라이드 이미지 추가
    private fun addSlideImage() {
        slideList.add(R.drawable.viewpager_image1)
        slideList.add(R.drawable.viewpager_image2)
        slideList.add(R.drawable.viewpager_image3)
    }

    //1-4. runnable 객체 정의
    private val runnable = Runnable {
        //맨 마지막 이미지에 도달하면 처음 이미지로 되돌아감 
        if (topSlideBannerViewPager2.currentItem == slideList.size - 1)
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