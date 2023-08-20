package com.example.goldenratio.cocktail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goldenratio.cocktail.models.*
import com.example.goldenratio.databinding.ActivityAddRatioBinding

val ratioNameList: ArrayList<String> = arrayListOf()   // 재료 이름
val ratioColorList: ArrayList<String> = arrayListOf()  // 색상

class AddRatioActivity : AppCompatActivity() {
    private lateinit var addRatioBinding: ActivityAddRatioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addRatioBinding = ActivityAddRatioBinding.inflate(layoutInflater)
        setContentView(addRatioBinding.root)

        // 리스트 초기화
        ratioItemList.clear()
        ratioNameList.clear()

        // 기본 색상
        ratioColorList.apply{
            add("#F9ED69")
            add("#F08A5D")
            add("#B83B5E")
            add("#6A2C70")
            add("#000000")
        }

        // 적용하기
        for (i in 0 ..ingredientNameList.size -1) {
            if (ingredientNameList.size != 0) {
                ratioItemList.apply {
                    add(
                        Ratio(
                            name = ingredientNameList[i], color = ratioColorList[i]
                        )
                    )
                }
            }
        }

        addRatioBinding.rvCheck.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        addRatioBinding.rvCheck.setHasFixedSize(true)

        addRatioBinding.rvCheck.adapter =
            AddRatioAdapter(ratioItemList)


        // 다음으로 이동
        addRatioBinding.btNext.setOnClickListener {
            val intent = Intent(this, RatioActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }
}