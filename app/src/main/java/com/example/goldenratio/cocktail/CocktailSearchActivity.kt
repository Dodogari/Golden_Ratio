package com.example.goldenratio.cocktail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goldenratio.R
import com.example.goldenratio.databinding.ActivitySearchBinding
import com.example.goldenratio.hangover.Ingredient
import com.example.goldenratio.hangover.IngredientActivity
import com.example.goldenratio.hangover.NewIngredientActivity
import com.example.goldenratio.hangover.ingredientList
import com.example.goldenratio.search.*
import java.net.URL


class CocktailSearchActivity : AppCompatActivity(), SearchInterface {

    private lateinit var searchBinding: ActivitySearchBinding
    val searchList2: ArrayList<Search> = arrayListOf()
    val rankList2: ArrayList<Rank> = arrayListOf()
    val searchNameList: ArrayList<SearchName> = arrayListOf()
    var gradientUrl : URL? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)

        if(searchList2.size == 0)
        {
            searchBinding.tvRecently.visibility = View.GONE
            searchBinding.rvRecently.visibility = View.GONE
        }

        rankList2.apply {
            add(Rank(R.drawable.ic_rank1, "소주"))
            add(Rank(R.drawable.ic_rank2, "상큼"))
            add(Rank(R.drawable.ic_rank3, "맥주"))
            add(Rank(R.drawable.ic_rank4, "달달"))
            add(Rank(R.drawable.ic_rank5, "젤리"))
        }
        searchBinding.rvRank.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchBinding.rvRank.setHasFixedSize(true)
        searchBinding.rvRank.adapter = RankAdapter(rankList = rankList2)

        // 이전 화면으로 이동
        searchBinding.btBack.setOnClickListener{
            finish()
        }

        // 재료추가 화면으로 이동
        searchBinding.btNewIngredient.setOnClickListener{
            val intent = Intent(this, NewIngredientActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        searchBinding.viewSearch.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            // 검색 버튼 누를 때 호출
            override fun onQueryTextSubmit(query: String): Boolean {
                search(query)
                if (query != null && !query.equals(""))
                {
                    searchBinding.tvRecently.isVisible = true
                    searchBinding.rvRecently.isVisible = true
                    searchRecently(query)
                }
                return true
            }

            // 검색창에서 글자가 변경이 일어날 때마다 호출
            override fun onQueryTextChange(newText: String): Boolean {
                //searchList(newText)
                return true
            }
        })
    }
    // 최근 검색어
    fun searchRecently(query: String){
        searchList2.apply {
            add(Search(search = query, R.drawable.ic_search_del))
        }

        searchBinding.rvRecently.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchBinding.rvRecently.setHasFixedSize(true)
        searchBinding.rvRecently.adapter = SearchAdapter(searchList = searchList2)
    }

//    // 검색
//    fun searchList(query: String){
//        searchNameList.apply {
//            add(SearchName(search = R.drawable.ic_search, name = query))
//        }
//
//        searchBinding.rvSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        searchBinding.rvSearch.setHasFixedSize(true)
//        searchBinding.rvSearch.adapter = SearchNameAdapter(searchNameList = searchNameList)
//    }

    // 서버에 값 보냄
    fun search(query: String) {
        SearchService(this).tryGetSearch(query)
        Log.d("tag", "검색 서버 요청")
    }

    // 서버 연결 성공
    override fun onSearchSuccess(response: SearchResponse) {
        gradientUrl = response.gradientImageUrl

        Log.d("tag", "서버 연결 완료")
        if(response.gradientImageUrl != null) {
            ingredientList.apply {
                add(
                    Ingredient(
                        response.gradientImageUrl,
                        response.gradientName,
                        R.drawable.ic_delete
                    )
                )
            }
            val intent = Intent(this, IngredientActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)

            Toast.makeText(this, "재료를 추가하였습니다.", Toast.LENGTH_SHORT).show()
        }

        if(response.gradientImageUrl == null || response.gradientImageUrl == null) {
            Log.d("tag", "${response.gradientName}")
            Log.d("tag", "${response.gradientImageUrl}")
            Toast.makeText(this, "재료를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 서버 연결 실패
    override fun onSearchFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Log.d("tag", "서버 연결 실패")
        //Toast.makeText(this, "재료를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
    }
}
