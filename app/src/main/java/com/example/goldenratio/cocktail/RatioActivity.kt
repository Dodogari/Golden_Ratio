package com.example.goldenratio.cocktail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goldenratio.databinding.ActivityRatioBinding

class RatioActivity : AppCompatActivity() {
    private lateinit var ratioBinding: ActivityRatioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ratioBinding = ActivityRatioBinding.inflate(layoutInflater)
        setContentView(ratioBinding.root)

        val ratioList: ArrayList<Ratio> = arrayListOf()

        ratioList.apply {
            add(Ratio("스프라이트"))
            add(Ratio("스프라이트"))
            add(Ratio("스프라이트"))
        }

        ratioBinding.rvIngredient.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ratioBinding.rvIngredient.setHasFixedSize(true)
        ratioBinding.rvIngredient.adapter = RatioAdapter(ratioList = ratioList)

        ratioBinding.btNext.setOnClickListener {

        }
    }
}