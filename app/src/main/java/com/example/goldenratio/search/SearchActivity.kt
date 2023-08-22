package com.example.goldenratio.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goldenratio.MainActivity
import com.example.goldenratio.R
import com.example.goldenratio.databinding.ActivitySearchBinding
import com.example.goldenratio.hangover.NewIngredientActivity
import com.example.goldenratio.hangover.ingredient_name


class SearchActivity : AppCompatActivity(), SearchInterface {

    private lateinit var searchBinding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)

        val searchList: ArrayList<Search> = arrayListOf()
        val rankList: ArrayList<Rank> = arrayListOf()

        searchList.apply {
            add(Search(searchBinding.viewSearch.toString(), R.drawable.ic_search_del))
        }

        searchBinding.rvSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchBinding.rvSearch.setHasFixedSize(true)

        searchBinding.rvSearch.adapter = SearchAdapter(searchList = searchList)

        rankList.apply {
            add(Rank(R.drawable.ic_rank1, "소주"))
            add(Rank(R.drawable.ic_rank2, "상큼"))
            add(Rank(R.drawable.ic_rank3, "맥주"))
            add(Rank(R.drawable.ic_rank4, "달달"))
            add(Rank(R.drawable.ic_rank5, "젤리"))
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

            // 서버에 값 보냄
            val name = ingredient_name.toString()

            SearchService(this).tryGetSearch(name)
            Toast.makeText(this, "서버 요청", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, NewIngredientActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }


    // 서버 연결 성공
    override fun onSearchSuccess(response: SearchResponse) {
        // 메인 화면으로 이동
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)

        Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
    }

    // 서버 연결 실패
    override fun onSearchFailure(message: String) {
        Log.d("error", "오류 : $message")
        Toast.makeText(this, "오류 : ${message}", Toast.LENGTH_SHORT).show()
    }
}