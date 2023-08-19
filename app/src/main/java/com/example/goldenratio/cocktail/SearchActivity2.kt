package com.example.goldenratio.cocktail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goldenratio.R
import com.example.goldenratio.databinding.ActivitySearchBinding
import com.example.goldenratio.search.Rank
import com.example.goldenratio.search.RankAdapter
import com.example.goldenratio.search.Search
import com.example.goldenratio.search.SearchAdapter

class SearchActivity2 : AppCompatActivity() {

    private lateinit var searchBinding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)

        val searchList: ArrayList<Search> = arrayListOf()
        val rankList: ArrayList<Rank> = arrayListOf()

        searchList.apply {
            add(Search("스프라이트", R.drawable.ic_search_del))
            add(Search("스프라이트", R.drawable.ic_search_del))
            add(Search("스프라이트", R.drawable.ic_search_del))
            add(Search("스프라이트", R.drawable.ic_search_del))
            add(Search("스프라이트", R.drawable.ic_search_del))
        }

        searchBinding.rvSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchBinding.rvSearch.setHasFixedSize(true)

        searchBinding.rvSearch.adapter = SearchAdapter(searchList = searchList)


        rankList.apply {
            add(Rank(R.drawable.ic_rank1, "스프라이트"))
            add(Rank(R.drawable.ic_rank2, "스프라이트"))
            add(Rank(R.drawable.ic_rank3, "스프라이트"))
            add(Rank(R.drawable.ic_rank4, "스프라이트"))
            add(Rank(R.drawable.ic_rank5, "스프라이트"))
        }

        searchBinding.rvRank.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchBinding.rvRank.setHasFixedSize(true)

        searchBinding.rvRank.adapter = RankAdapter(rankList = rankList)

        // 이전 화면으로 이동
        searchBinding.btBack.setOnClickListener{
            finish()
        }

        // 재료추가 화면으로 이동
        searchBinding.btNewIngredient.setOnClickListener{
            val intent = Intent(this, NewIngredientActivity2::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }
}