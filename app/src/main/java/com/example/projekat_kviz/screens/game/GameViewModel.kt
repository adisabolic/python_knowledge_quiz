package com.example.projekat_kviz.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class GameViewModel(val difficulty: Int, var maxScore: Int) : ViewModel() {

    val checkedAns = mutableListOf<Boolean>()
    var jokerUsed : Boolean
    private var timer: CountDownTimer
    private var nmbrEasyTotal : Int = 0
    private var nmbrMedTotal : Int = 0
    private var nmbrHardTotal : Int = 0
    private var nmbrEasyCurr : Int = 0
    private var nmbrMedCurr : Int = 0
    private var nmbrHardCurr : Int = 0

    // Countdown time
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    companion object {
        // Time when the game is over
        private const val DONE = 0L

        // Countdown time interval
        private const val ONE_SECOND = 1000L

        private const val EASY_e = 0.5
        private const val EASY_m = 0.3
        private const val EASY_h = 0.2

        private const val MED_e = 0.3
        private const val MED_m = 0.5
        private const val MED_h = 0.2

        private const val HARD_e = 0.2
        private const val HARD_m = 0.3
        private const val HARD_h = 0.5
    }

    private val _dataReadSuccesfully = MutableLiveData<Boolean>()
    val dataReadSuccesfully: LiveData<Boolean>
        get() = _dataReadSuccesfully

    private var _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _nmbrQuestion = MutableLiveData<Int>()
    val nmbrQuestion: LiveData<Int>
        get() = _nmbrQuestion

    private val _question = MutableLiveData<String>()
    val question: LiveData<String>
        get() = _question

    private val _code = MutableLiveData<String>()
    val code: LiveData<String>
        get() = _code

    private val _type = MutableLiveData<Int>()
    val type: LiveData<Int>
        get() = _type

    private val _answer = MutableLiveData<List<String>>()
    val answer: LiveData<List<String>>
        get() = _answer

    private val _currTimer = MutableLiveData<Long>()
    val currTimer: LiveData<Long>
        get() = _currTimer

    private val _timeUp = MutableLiveData<Boolean>()
    val timeUp: LiveData<Boolean>
        get() = _timeUp

    private lateinit var typeList: MutableList<Int>
    private lateinit var questionList: MutableList<String>
    private lateinit var answerList: MutableList<List<String>>
    private lateinit var correctAnswerList: MutableList<List<Boolean>>
    private lateinit var correctTekstList: MutableList<List<String>>
    private lateinit var timerList: MutableList<Long>

    private var correctAnswer: List<Boolean>
    private var correctTekst: List<String>

    private interface FirestoreCallback {
        fun onCallback(f: () -> (Unit))
    }

    private fun readData(firestoreCallback: FirestoreCallback) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("pitanje")
        var diffTemp : Int
        docRef.get()
            .addOnSuccessListener { documents ->
                val documentsList = documents.toMutableList()
                documentsList.shuffle()
                if(documentsList.size < maxScore) {
                    maxScore = documentsList.size
                    initializeQuestionsByDifficulty()
                }

                for (document in documentsList) {
                    diffTemp = document.data["tezina"].toString().toInt()
                    if(diffTemp == 0 && nmbrEasyCurr < nmbrEasyTotal
                        || diffTemp == 1 && nmbrMedCurr < nmbrMedTotal
                        || diffTemp == 2 && nmbrHardCurr < nmbrHardTotal ) {
                            questionList.add(document.data["tekst"].toString())
                            typeList.add(document.data["tip"].toString().toInt())
                            answerList.add(document.data["ponudjen"] as List<String>)
                            correctAnswerList.add(document.data["tacanPonudjen"] as List<Boolean>)
                            correctTekstList.add(document.data["tacanTekst"] as List<String>)
                            timerList.add(ONE_SECOND * (document.data["tajmer"] as Long))

                            when(diffTemp) {
                                0 -> nmbrEasyCurr += 1
                                1 -> nmbrMedCurr += 1
                                2 -> nmbrHardCurr += 1
                            }
                    }
                    else if(nmbrEasyCurr + nmbrMedCurr + nmbrHardCurr == maxScore)
                        break
                }
                if(questionList.size < maxScore) {
                    maxScore = nmbrEasyCurr + nmbrMedCurr + nmbrHardCurr
                }
                firestoreCallback.onCallback({ firstQuestion() })
            }
            .addOnFailureListener { exception ->
                Log.d("baza", "get failed with ", exception)
            }
    }

    private fun resetLists() {
        questionList = mutableListOf()
        typeList = mutableListOf()
        answerList = mutableListOf()
        correctAnswerList = mutableListOf()
        correctTekstList = mutableListOf()
        timerList = mutableListOf()

        readData(object : FirestoreCallback {
            override fun onCallback(f: () -> Unit) {
                f()
            }
        })
    }

    init {
        _eventGameFinish.value = false

        _nmbrQuestion.value = 0
        _score.value = 0

        _question.value = ""
        _answer.value = listOf()
        _type.value = 0
        correctTekst = listOf()
        correctAnswer = listOf()
        jokerUsed = false
        _dataReadSuccesfully.value = false
        _timeUp.value = false

        initializeQuestionsByDifficulty()

        timer = object : CountDownTimer(0, ONE_SECOND) {
            override fun onFinish() {
            }

            override fun onTick(millisUntilFinished: Long) {
            }
        }

        Log.i("GameViewModel", "GameViewModel created!")
        resetLists()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
        timer.cancel()
    }

    private fun firstQuestion() {
        nextQuestion()
        _dataReadSuccesfully.value = true
    }

    private fun nextQuestion() {
        timer.cancel()
        _nmbrQuestion.value = (_nmbrQuestion.value)?.plus(1)
        if (questionList.isNotEmpty()) {
            getCode()
            _answer.value = answerList.removeAt(0)
            _type.value = typeList.removeAt(0)
            correctAnswer = correctAnswerList.removeAt(0)
            correctTekst = correctTekstList.removeAt(0)
            _currTimer.value = timerList.removeAt(0)
            _timeUp.value = false

            // Creates a timer which triggers the end of the game when it finishes
            if(currTimer.value != 0L) {
                timer = object : CountDownTimer(currTimer.value!!, ONE_SECOND) {

                    override fun onTick(millisUntilFinished: Long) {
                        _currentTime.value = millisUntilFinished / ONE_SECOND
                    }

                    override fun onFinish() {
                        _currentTime.value = DONE
                        nextQuestion()
                        _timeUp.value = true
                    }
                }
                timer.start()
            } else { _currentTime.value = 0 }
        } else {
            finishGame()
        }
    }

    fun processTextQuestion(ans: String) {

        if(ans.isNotBlank()) {
            var correct = false
            val processedAns = ans.toLowerCase(Locale.ROOT).trim()

            if (processedAns in correctTekst) {
                correct = true
            }

            if (correct) {
                _score.value = (_score.value)?.plus(1)
            }
            nextQuestion()
        }
    }

    fun processRadiobuttonQuestion(ans: Int){

        if(correctAnswer[ans]){
            _score.value = (_score.value)?.plus(1)
        }
        nextQuestion()
    }

    fun processCheckboxQuestion(){

        var answered = false
        var correct = true

        for(i in 0 until checkedAns.size){
            if(checkedAns[i]){
                answered = true
                if(!correctAnswer[i]) correct = false
            } else {
                if(correctAnswer[i]) correct = false
            }
        }

        if(answered) {
            if (correct) {
                _score.value = (_score.value)?.plus(1)
            }
            nextQuestion()
        }
    }

    private fun finishGame(){
        _eventGameFinish.value = true
    }

    fun usedJoker() {
        jokerUsed = true
    }

    private fun initializeQuestionsByDifficulty() {
        when(difficulty) {
            0 -> {nmbrMedTotal = (EASY_m * maxScore).toInt(); nmbrHardTotal = (EASY_h * maxScore).toInt()
                nmbrEasyTotal = maxScore - nmbrMedTotal - nmbrHardTotal}
            1 -> {nmbrEasyTotal = (MED_e * maxScore).toInt(); nmbrHardTotal = (MED_h * maxScore).toInt()
                nmbrMedTotal = maxScore - nmbrEasyTotal - nmbrHardTotal}
            2 -> {nmbrEasyTotal = (HARD_e * maxScore).toInt(); nmbrMedTotal = (HARD_m * maxScore).toInt()
                nmbrHardTotal = maxScore - nmbrEasyTotal - nmbrMedTotal}
        }
    }

    private fun getCode(){
        val pom = questionList.removeAt(0)

        if (pom.contains("/code/")) {
            val indexS = pom.indexOf("/code/")
            val indexE = pom.indexOf("/-code/")
            _code.value = pom.subSequence(indexS.plus(6), indexE).toString()
            if(indexS != 0)
                _question.value = pom.subSequence(0, indexS).toString()
            else _question.value = pom.subSequence(indexE.plus(7), pom.length).toString()
        } else {
            _code.value = ""
            _question.value = pom
        }
    }
}