package com.example.projekat_kviz.screens.title

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.projekat_kviz.R
import com.example.projekat_kviz.databinding.TitleFragmentBinding


class TitleFragment : Fragment() {

    private lateinit var binding: TitleFragmentBinding
    private lateinit var nmbrQuestions: EditText
    private lateinit var difficulty: RadioGroup

    companion object {
        private const val EASY = 0
        private const val MEDIUM = 1
        private const val HARD = 2
        private val DEFAULT =
            (when(R.string.default_difficulty.toString()) {
                "easy" -> 0
                "medium" -> 1
                "hard" -> 2
                else -> 1
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.title_fragment, container, false)

        nmbrQuestions = binding.etNumberOfQuestions
        difficulty = binding.radioGroupDifficulty

        binding.playButton.setOnClickListener {
            startGame()
        }

        binding.etNumberOfQuestions.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(context, view)
            }
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,requireView().findNavController())
                ||super.onOptionsItemSelected(item)
    }

    private fun hideKeyboard(context: Context?, view: View?) {
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun startGame() {
        val nmbrQuestionsInt : Int = nmbrQuestions.text.toString().toIntOrNull() ?: R.string.default_number_of_questions.toInt()

        val difficultyInt =
            (when (difficulty.checkedRadioButtonId) {
                R.id.radioEasy -> EASY
                R.id.radioMed -> MEDIUM
                R.id.radioHard -> HARD
                else -> DEFAULT
            })
        NavHostFragment
            .findNavController(this)
                .navigate(TitleFragmentDirections.actionTitleFragmentToGameFragment(difficultyInt, nmbrQuestionsInt))
    }
}
