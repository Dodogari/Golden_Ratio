package com.example.goldenratio.hangover

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goldenratio.R
import com.example.goldenratio.databinding.ActivityIngredientBinding
import com.example.goldenratio.search.SearchActivity


val ingredientList: ArrayList<Ingredient> = arrayListOf()

class IngredientActivity : AppCompatActivity() {
    private lateinit var ingredientBinding: ActivityIngredientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ingredientBinding = ActivityIngredientBinding.inflate(layoutInflater)
        setContentView(ingredientBinding.root)

        // 이미지 저장
        ingredientList.apply {
            if (ingredient_name != null)
            {
                add(Ingredient(url_ingredient, ingredient_name.toString(), R.drawable.ic_delete))
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
            val intent = Intent(this, SearchActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        // 다음 화면으로 이동
        ingredientBinding.btNext.setOnClickListener{
            val intent = Intent(this, AddHangoverActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }
}