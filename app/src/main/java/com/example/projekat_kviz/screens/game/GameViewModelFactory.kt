package com.example.projekat_kviz.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class GameViewModelFactory(private val difficulty: Int, private val nmbrQuestions: Int) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GameViewModel::class.java)){
            return GameViewModel(difficulty, nmbrQuestions) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}