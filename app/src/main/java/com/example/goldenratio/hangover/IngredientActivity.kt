package com.example.goldenratio.hangover

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goldenratio.R
import com.example.goldenratio.databinding.ActivityIngredientBinding

class IngredientActivity : AppCompatActivity() {
    private lateinit var ingredientBinding: ActivityIngredientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ingredientBinding = ActivityIngredientBinding.inflate(layoutInflater)
        setContentView(ingredientBinding.root)

        val ingredientList: ArrayList<Ingredient> = arrayListOf()

        ingredientList.apply {
            add(Ingredient(img_uri,"스프라이트", R.drawable.ic_delete))
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

//    // 화면 이동
//    fun changeFragment(index: Int){
//        when(index) {
//            // 재료 검색 페이지
//            1 -> {
//                supportFragmentManager
//                    .beginTransaction()
//                    .replace(ingredientBinding.ingredientFrame.id, SearchFragment())
//                    .commitAllowingStateLoss()
//            }
//        }
//    }
}