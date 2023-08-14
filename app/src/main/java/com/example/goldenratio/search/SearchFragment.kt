package com.example.goldenratio.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goldenratio.hangover.NewIngredientActivity
import com.example.goldenratio.R
import com.example.goldenratio.databinding.FragmentSearchBinding
import com.example.goldenratio.hangover.IngredientActivity
import com.example.goldenratio.hangover.NewHangoverActivity

class SearchFragment : Fragment() {
    private lateinit var searchBinding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchBinding = FragmentSearchBinding.inflate(inflater, container, false)

        val searchList: ArrayList<Search> = arrayListOf()
        val rankList: ArrayList<Rank> = arrayListOf()
        val activity = activity as NewHangoverActivity

        searchList.apply {
            add(Search("스프라이트", R.drawable.ic_search_del))
            add(Search("스프라이트", R.drawable.ic_search_del))
            add(Search("스프라이트", R.drawable.ic_search_del))
        }

        searchBinding.rvSearch.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        searchBinding.rvSearch.setHasFixedSize(true)

        searchBinding.rvSearch.adapter = SearchAdapter(searchList = searchList)


        rankList.apply {
            add(Rank(R.drawable.ic_rank1, "스프라이트"))
            add(Rank(R.drawable.ic_rank2, "스프라이트"))
            add(Rank(R.drawable.ic_rank3, "스프라이트"))
            add(Rank(R.drawable.ic_rank4, "스프라이트"))
            add(Rank(R.drawable.ic_rank5, "스프라이트"))
        }

        searchBinding.rvRank.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        searchBinding.rvRank.setHasFixedSize(true)

        searchBinding.rvRank.adapter = RankAdapter(rankList = rankList)

        // 이전 화면으로 이동
        searchBinding.btBack.setOnClickListener{
            val intent = Intent(getActivity(), IngredientActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        // 재료추가 화면으로 이동
        searchBinding.btNewIngredient.setOnClickListener{
            val intent = Intent(getActivity(), NewIngredientActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        return searchBinding.root
    }
}