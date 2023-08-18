package com.example.goldenratio.cocktail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goldenratio.MainActivity
import com.example.goldenratio.cocktail.models.Ratio
import com.example.goldenratio.cocktail.models.RatioAdapter
import com.example.goldenratio.cocktail.models.RatioCup
import com.example.goldenratio.cocktail.models.RatioCupAdapter
import com.example.goldenratio.databinding.ActivityRatioBinding

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

        ratioList.apply {
            add(Ratio("스프라이트", "#F9ED69"))
            add(Ratio("스프라이트", "#B83B5E"))
            add(Ratio("스프라이트", "#6A2C70"))
        }

        // 204/ratioList.size
        // 204/3 = 68
        ratioCupList.apply{
            add(RatioCup(color = "#F9ED69", 204f/ratioList.size))
            add(RatioCup(color = "#B83B5E", 204f/ratioList.size))
            add(RatioCup(color = "#6A2C70", 204f/ratioList.size))
        }


//        for (i in 0 ..ratioList.size -1) {
//            if (ratioList.size != 0) {
//                ratioCupList.apply{
//                    add(
//                        RatioCup(
//                            color = "#F9ED69",
//                        )
//                    )
//                }
//            }
//        }

        ratioBinding.rvIngredient.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ratioBinding.rvIngredient.setHasFixedSize(true)
        ratioBinding.rvIngredient.adapter = RatioAdapter(ratioList = ratioList)

        ratioBinding.rvRatioCup.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ratioBinding.rvRatioCup.setHasFixedSize(true)
        ratioBinding.rvRatioCup.adapter = RatioCupAdapter(ratioCupList = ratioCupList)
    }
}