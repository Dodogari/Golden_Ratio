package com.example.goldenratio.cocktail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.goldenratio.databinding.FragmentIngredientBinding

class IngredientFragment : Fragment() {
    private lateinit var ingredientBinding: FragmentIngredientBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ingredientBinding = FragmentIngredientBinding.inflate(inflater, container, false)

        return ingredientBinding.root
    }
}