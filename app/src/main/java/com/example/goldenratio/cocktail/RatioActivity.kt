package com.example.goldenratio.cocktail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.goldenratio.MainActivity
import com.example.goldenratio.R
import com.example.goldenratio.cocktail.models.*
import com.example.goldenratio.databinding.ActivityRatioBinding

val ratioIntList: ArrayList<Int> = arrayListOf()   // 비율
var sum = 0
var intRatio = 0
var ratio = 0

class RatioActivity : AppCompatActivity() {
    private lateinit var ratioBinding: ActivityRatioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ratioBinding = ActivityRatioBinding.inflate(layoutInflater)
        setContentView(ratioBinding.root)

        val ratioList: ArrayList<Ratio> = arrayListOf()
        val ratioCupList: ArrayList<RatioCup> = arrayListOf()

        // 메인 화면으로 이동
        ratioBinding.btNext.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        // 얼음 추가
        ratioBinding.btIce.setOnClickListener {
            // 얼음 보이기
            if(ratioBinding.btIce.isSelected){
                ratioBinding.imgIce.visibility = View.INVISIBLE
                ratioBinding.btIce.isSelected = false
            }
            // 얼음 숨기기
            else{
                ratioBinding.imgIce.visibility = View.VISIBLE
                ratioBinding.btIce.isSelected = true
            }
        }

        // 아래 리사이클러뷰 적용하기
        for (i in 0 ..ratioNameList.size -1) {
            if (ratioNameList.size != 0) {
                ratioList.apply {
                    add(
                        Ratio(
                            name = ratioNameList[i], color = ratioColorList[i]
                        )
                    )
                }
            }
        }

        ratioBinding.rvIngredient.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ratioBinding.rvIngredient.setHasFixedSize(true)
        ratioBinding.rvIngredient.adapter = RatioAdapter(ratioList = ratioList)


        for (i in 0 ..ratioNameList.size -1) {
            if (ratioNameList.size != 0) {
                ratioIntList.apply {
                    add(ratio)
                }
            }
        }

        Log.d("tag", "ratioIntListAdapter: {$ratioIntList}")

        for (i in 0 ..ratioNameList.size -1) {
            if (ratioNameList.size != 0) {
                ratioCupList.apply {
                    add(
                        RatioCup(
                            color = ratioColorList[i], size = 204f/10 * ratioIntList[i]
                        )
                    )
                }
            }
        }

        var swipe = findViewById<SwipeRefreshLayout>(R.id.swipe)
        swipe.setOnRefreshListener {
            val intent = intent
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
            swipe.isRefreshing = false
        }
        ratioBinding.rvRatioCup.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ratioBinding.rvRatioCup.setHasFixedSize(true)
        ratioBinding.rvRatioCup.adapter = RatioCupAdapter(ratioCupList = ratioCupList)
    }
}