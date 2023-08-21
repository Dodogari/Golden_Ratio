package com.example.goldenratio.cocktail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goldenratio.R
import com.example.goldenratio.cocktail.models.Ratio
import com.example.goldenratio.databinding.ActivityIngredientBinding
import com.example.goldenratio.hangover.*

val ratioItemList: ArrayList<Ratio> = arrayListOf()    // 아이템

class IngredientActivity2 : AppCompatActivity() {
    private lateinit var ingredientBinding: ActivityIngredientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ingredientBinding = ActivityIngredientBinding.inflate(layoutInflater)
        setContentView(ingredientBinding.root)

        // 리스트 초기화
        ratioItemList.clear()

        // 이미지 저장
        ingredientList.apply {
            if (ingredient_name != null)
            {
                add(Ingredient(url_ingredient, ingredient_name.toString(), R.drawable.ic_delete))
            }
        }

        // 이름 저장
        ingredientNameList.apply {
            if (ingredient_name != null)
            {
                add(ingredient_name.toString())
            }
        }

        ingredientBinding.rvIngredient.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ingredientBinding.rvIngredient.setHasFixedSize(true)

        ingredientBinding.rvIngredient.adapter = IngredientAdapter(ingredientList = ingredientList)

        // 이전 화면으로 이동
        ingredientBinding.btBack.setOnClickListener{
            finish()
        }

        // 검색 화면으로 이동
        ingredientBinding.btAdd.setOnClickListener{
            val intent = Intent(this, SearchActivity2::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        // 다음 화면으로 이동
        ingredientBinding.btNext.setOnClickListener{
            val intent = Intent(this, AddRatioActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }
}