package com.example.projekat_kviz.screens.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ScoreViewModelFactory (private val finalScore: Int, private val nmbrQuestion: Int,
                             private val jokerUsed: Boolean, private val difficulty: String)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel(finalScore, nmbrQuestion, jokerUsed, difficulty) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}