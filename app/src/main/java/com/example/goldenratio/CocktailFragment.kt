package com.example.goldenratio

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.goldenratio.databinding.FragmentCocktailBinding
import me.relex.circleindicator.CircleIndicator3
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CocktailFragment : Fragment() {
    private lateinit var cocktailBinding: FragmentCocktailBinding

    //#1. 상단 슬라이드 배너 변수
    private var slideList: MutableList<Int> = mutableListOf()                       //슬라이드에 보여질 이미지 리스트
    private lateinit var topSlideBannerViewPager2: ViewPager2                       //슬라이드 뷰페이저 리스트
    private lateinit var topSlideBannerAdapter: TopSlideBannerAdapter               //슬라이드 뷰페이저 어댑터
    private lateinit var indicator3: CircleIndicator3                               //슬라이드 인디케이터
    private lateinit var handler: Handler                                           //핸들러 (메시지 전달)

    //#2. 칵테일 리스트 변수
    private var recyclerViewBoardAdapter: RecyclerViewBoardAdapter? = null    //칵테일 리사이클러뷰 리스트 어댑터
    private var cocktailList = arrayListOf<BoardData>()

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
        indicator3 = cocktailBinding.topSlideBannerIndicator
        indicator3.setViewPager(topSlideBannerViewPager2)

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
                indicator3.animatePageSelected(position)        //인디케이터와 뷰페이저 연결

                handler.removeCallbacks(runnable)               //슬라이드 멈춤
                handler.postDelayed(runnable, 3000)    //3초에 한번 슬라이드
            }
        })
        //testCocktailList()
        
        //#2. 서버 통신: 칵테일 보드 내용 받아오기
        //2-1. 응답
        val cocktailListContent = RegisterClient.registerService.getCocktailAll()
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
                recyclerViewBoardAdapter = RecyclerViewBoardAdapter(cocktailList)
                cocktailBinding.listCocktail.adapter = recyclerViewBoardAdapter

                recyclerViewBoardAdapter!!.setOnClickListener(object : RecyclerViewBoardAdapter.OnClickListener {
                    //5-1. 상세 메뉴 액티비티로 전환
                    override fun onClick(position: Int) {
                        val itemIntent = Intent(activity, CocktailItemActivity::class.java)
                        itemIntent.putExtra("boardId", cocktailList[position].boardId)
                        startActivity(itemIntent)
                    }

                    //5-2. 좋아요 클릭
                    override fun likeOnClick(position: Int) {
                        /*cocktailList[position].likeCount = !cocktailList[position].likeCheck
                        if(cocktailList[position].likeCheck) {
                            cocktailList[position].like++
                            recyclerViewCocktailAdapter!!.notifyItemChanged(cocktailList[position].like, cocktailList[position].like++)
                        }
                        else {
                            cocktailList[position].like--
                            recyclerViewCocktailAdapter!!.notifyItemChanged(cocktailList[position].like, cocktailList[position].like--)
                        }*/
                    }

                })
            }

            override fun onFailure(call: Call<ArrayList<BoardData>>, t: Throwable) {
                Toast.makeText(activity, "칵테일 데이터 불러오기를 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        //#4. 라디오 버튼 클릭 -> 라디오 버튼 난리
        cocktailBinding.radioCocktailAll.isChecked = true
/*
        //#5. 아이템 클릭 설정
        recyclerViewBoardAdapter!!.setOnClickListener(object : RecyclerViewBoardAdapter.OnClickListener {
            //5-1. 상세 메뉴 액티비티로 전환
            override fun onClick(position: Int) {
                val itemIntent = Intent(activity, CocktailItemActivity::class.java)
                itemIntent.putExtra("position", position)
                startActivity(itemIntent)
            }

            //5-2. 좋아요 클릭
            override fun likeOnClick(position: Int) {
                /*cocktailList[position].likeCount = !cocktailList[position].likeCheck
                if(cocktailList[position].likeCheck) {
                    cocktailList[position].like++
                    recyclerViewCocktailAdapter!!.notifyItemChanged(cocktailList[position].like, cocktailList[position].like++)
                }
                else {
                    cocktailList[position].like--
                    recyclerViewCocktailAdapter!!.notifyItemChanged(cocktailList[position].like, cocktailList[position].like--)
                }*/
            }

        })*/
        /*
        //수정된 내용 받아오기
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK) {
                //콘텐츠 내용 값 가져오기
                val title = it.data?.getStringExtra("title")
                val rating = it.data?.getFloatExtra("rating", 0f)
                val thumbnail = it.data?.getIntExtra("thumbnail", 0)
                val like = it.data?.getIntExtra("like", 0)
                val position = it.data?.getIntExtra("position", 0)

                cocktailList[position!!].title = title.toString()
                cocktailList[position].thumbnail = thumbnail!!.toInt()
                cocktailList[position].like = like!!.toInt()
                cocktailList[position].rating = rating!!.toFloat()
            }
        }
        //칵테일 리스트 아이템 클릭 시
        recyclerViewCocktailAdapter!!.setOnClickListener(object : RecyclerViewCocktailAdapter.OnClickListener {
            //상세 페이지 불러오기
            override fun onClick(position: Int) {
                val itemIntent = Intent(activity, CocktailItemActivity::class.java)
                itemIntent.putExtra("title", cocktailList[position].title)
                itemIntent.putExtra("thumbnail", cocktailList[position].thumbnail)
                itemIntent.putExtra("like", cocktailList[position].like)
                itemIntent.putExtra("rating", cocktailList[position].rating)
                itemIntent.putExtra("writtenDate", cocktailList[position].writtenDate)
                itemIntent.putExtra("sweetLevel", cocktailList[position].sweetLevel)
                itemIntent.putExtra("alcoholLevel", cocktailList[position].alcoholLevel)
                itemIntent.putParcelableArrayListExtra("gradientList", cocktailList[position].gradientList)
                itemIntent.putExtra("recipeContent", cocktailList[position].recipeContent)
                itemIntent.putParcelableArrayListExtra("balanceList", cocktailList[position].balanceList)
                itemIntent.putParcelableArrayListExtra("reviewList", cocktailList[position].reviewList)
                itemIntent.putExtra("position", position)                   //배열 위치 넘기기
                resultLauncher.launch(itemIntent)
            }

            override fun likeOnClick(position: Int) {
                cocktailList[position].likeCheck = !cocktailList[position].likeCheck
                if(cocktailList[position].likeCheck) {
                    cocktailList[position].like++
                    recyclerViewCocktailAdapter!!.notifyItemChanged(cocktailList[position].like, cocktailList[position].like++)
                }
                else {
                    cocktailList[position].like--
                    recyclerViewCocktailAdapter!!.notifyItemChanged(cocktailList[position].like, cocktailList[position].like--)
                }
            }

        })*/
    }

    //1-1. 슬라이드 이미지 추가
    private fun addSlideImage() {
        slideList.add(R.drawable.view_test1)
        slideList.add(R.drawable.view_test2)
        slideList.add(R.drawable.view_test3)
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

/*

    internal class FetchImage(var URL: String) : Thread() {
        var bitmap: Bitmap? = null
        override fun run() {
            mainHandler.post(Runnable {
                progressDialog = ProgressDialog(this@MainActivity)
                progressDialog.setMessage("Getting your pic....")
                progressDialog.setCancelable(false)
                progressDialog.show()
            })
            var inputStream: InputStream? = null
            try {
                inputStream = URL(URL).openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mainHandler.post(Runnable {
                if (progressDialog.isShowing()) progressDialog.dismiss()
                binding.imageView.setImageBitmap(bitmap)
            })
        }
    }*/
}