package com.example.projekat_kviz.screens.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int, nmbrQuestion: Int, val jokerUsed: Boolean, val difficulty: String) : ViewModel() {

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _nmbrQuestion = MutableLiveData<Int>()
    val nmbrQuestion: LiveData<Int>
        get() = _nmbrQuestion

    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean>
        get() = _eventPlayAgain

    init {
        _score.value = finalScore
        _nmbrQuestion.value = nmbrQuestion
    }

    fun onPlayAgain() {
        _eventPlayAgain.value = true
    }

    fun onPlayAgainComplete() {
        _eventPlayAgain.value = false
    }
}