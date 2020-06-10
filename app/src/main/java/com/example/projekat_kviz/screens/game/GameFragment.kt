package com.example.projekat_kviz.screens.game

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.projekat_kviz.R
import com.example.projekat_kviz.databinding.GameFragmentBinding
import io.github.kbiakov.codeview.CodeView
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.classifier.CodeProcessor
import io.github.kbiakov.codeview.highlight.ColorTheme


class GameFragment : Fragment() {
    private lateinit var binding: GameFragmentBinding
    private lateinit var viewModel: GameViewModel
    private lateinit var viewModelFactory : GameViewModelFactory
    private lateinit var codeView: CodeView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)

        Log.i("MainActivity","Created game fragment.")

        viewModelFactory = GameViewModelFactory(GameFragmentArgs.fromBundle(requireArguments()).difficulty,
            GameFragmentArgs.fromBundle(requireArguments()).nmbrQuestions)

        Log.i("MainActivity","Created view model factory.")

        viewModel =  ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        Log.i("MainActivity","Created view model.")

        binding.gameViewModel = viewModel
        Log.i("MainActivity","Binding odradjen")

        binding.tvDifficulty.text =
            (when(viewModel.difficulty) {
                0 -> resources.getString(R.string.difficulty_easy)
                1 -> resources.getString(R.string.difficulty_med)
                2 -> resources.getString(R.string.difficulty_hard)
                else -> resources.getString(R.string.default_difficulty)
            })
        Log.i("MainActivity","Text postavljen")

        binding.buttonAnswer.setOnClickListener {
            Log.i("testAnswerButton", "Answer button clicked.")
            (when(viewModel.type.value) {
                1 -> {
                    viewModel.processTextQuestion(binding.layoutTextAnswer.inputTextAnswer.text.toString())
                    binding.layoutTextAnswer.inputTextAnswer.setText("")
                }
                2 -> {
                    val buttonIndex = binding.layoutMultipleChoice.radioBtns.checkedRadioButtonId
                    if(buttonIndex != -1)
                        viewModel.processRadiobuttonQuestion(buttonIndex)
                }
                3 -> viewModel.processCheckboxQuestion()
            })
            renderScreen()
        }

        // Observer for the Game finished event
        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { hasFinished ->
            if (hasFinished) gameFinished()
        })

        // Observer for the dataReadSuccesfully property
        viewModel.dataReadSuccesfully.observe(viewLifecycleOwner, Observer { hasRead ->
            if (hasRead) {
                CodeProcessor.init(this.context)
                codeView = binding.layoutQuestion.codeView
                this.context?.let {
                    Options.Default.get(it)
                        .withLanguage("python")
                        .withCode("")
                        .withTheme(ColorTheme.SOLARIZED_LIGHT)
                }?.let {
                    codeView.setOptions(
                        it
                    )
                }
                renderScreen()
            }
        })

        viewModel.timeUp.observe(viewLifecycleOwner, Observer { timeUp ->
            if (timeUp) renderScreen()
        })

        // Hide keyboard when text box is not focused
        binding.layoutTextAnswer.inputTextAnswer.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(context, view)
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun renderScreen(){

        val sizeQ = viewModel.question.value?.length ?: 0
        when{
            sizeQ > 70 -> binding.layoutQuestion.questionText.textSize = 14F
            sizeQ > 50 -> binding.layoutQuestion.questionText.textSize = 18F
            else -> binding.layoutQuestion.questionText.textSize = 22F
        }

        if(viewModel.code.value.isNullOrEmpty()){
            binding.layoutQuestion.codeView.visibility = View.GONE
        } else {
            binding.layoutQuestion.codeView.visibility = View.VISIBLE
            binding.layoutQuestion.codeView.setCode(viewModel.code.value.toString())
        }

        when(viewModel.type.value) {
            1 -> {
                binding.layoutTextAnswer.root.visibility = View.VISIBLE
                binding.layoutMultipleChoice.root.visibility = View.GONE
                binding.layoutCheckBox.root.visibility = View.GONE
            }
            2 -> {
                processRadioButtons()
                binding.layoutMultipleChoice.root.visibility = View.VISIBLE
                binding.layoutTextAnswer.root.visibility = View.GONE
                binding.layoutCheckBox.root.visibility = View.GONE
            }
            3 -> {
                processChecbox()
                binding.layoutCheckBox.root.visibility = View.VISIBLE
                binding.layoutTextAnswer.root.visibility = View.GONE
                binding.layoutMultipleChoice.root.visibility = View.GONE
            }
            else -> {
                binding.layoutTextAnswer.root.visibility = View.GONE
                binding.layoutMultipleChoice.root.visibility = View.GONE
                binding.layoutCheckBox.root.visibility = View.GONE
            }
        }
    }

    private fun gameFinished(){
        val difficultyText : String = when(viewModel.difficulty) {
            0 -> resources.getString(R.string.difficulty_easy)
            1 -> resources.getString(R.string.difficulty_med)
            2 -> resources.getString(R.string.difficulty_hard)
            else -> resources.getString(R.string.default_difficulty)}

        val action = GameFragmentDirections
            .actionEndGame( viewModel.maxScore, viewModel.jokerUsed,
                viewModel.score.value?:0, difficultyText)
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun processRadioButtons(){

        val radioGroup = binding.layoutMultipleChoice.radioBtns
        val count = radioGroup.childCount
        if (count > 0) {
            for (i in count - 1 downTo 0) {
                val o = radioGroup.getChildAt(i)
                if (o is RadioButton) {
                    radioGroup.removeViewAt(i)
                }
            }
        }
        radioGroup.clearCheck()

        for (i in 0 until (viewModel.answer.value?.size ?: 0)){
            val radioBtn = RadioButton(this.context)
            radioBtn.layoutParams= LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            radioBtn.text = viewModel.answer.value?.get(i).toString()
            radioBtn.textSize = 16F
            radioBtn.id = i
            radioBtn.typeface = Typeface.createFromAsset(context?.assets, "firacode_regular.ttf")
            radioGroup.addView(radioBtn)
        }
    }

    private fun processChecbox(){
        val cbContainer = binding.layoutCheckBox.cbContainer

        val count = cbContainer.childCount
        if (count > 0) {
            for (i in count - 1 downTo 0) {
                val o = cbContainer.getChildAt(i)
                if (o is CheckBox) {
                    cbContainer.removeViewAt(i)
                }
            }
        }
        viewModel.checkedAns.clear()

        for (i in 0 until (viewModel.answer.value?.size ?: 0)){
            val checkBox = CheckBox(this.context)
            checkBox.layoutParams= LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            checkBox.text = viewModel.answer.value?.get(i) ?: ""
            checkBox.textSize = 16F
            checkBox.typeface = Typeface.createFromAsset(context?.assets, "firacode_regular.ttf")
            checkBox.id = i
            viewModel.checkedAns.add(false)
            checkBox.setOnCheckedChangeListener { _, _ ->
                viewModel.checkedAns[i] = checkBox.isChecked
            }
            cbContainer.addView(checkBox)
        }
    }

    private fun hideKeyboard(context: Context?, view: View?) {
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun getShareIntentText(text: String?, answers: List<String>?, type: Int?): Intent {
        val helpIntent = Intent(Intent.ACTION_SEND)
        var stringAnswers = ""
        var message = getString(R.string.joker_help_text, text)
        if(answers != null) {
            for(option in answers) {
                stringAnswers = "$stringAnswers- $option\n"
            }
            if(type == 2)
                message = "$message ${getString(R.string.one_answer)} \n$stringAnswers"
            else if(type == 3)
                message = "$message ${getString(R.string.more_answers)} \n$stringAnswers"
        }

        helpIntent.setType("text/plain")
            .putExtra(
                Intent.EXTRA_TEXT,
                message
            )
        return helpIntent
    }

    private fun getShareIntentCall(): Intent {
        return Intent(Intent.ACTION_DIAL)
    }

    private fun shareSuccess(type: Int) {
        if(type == 0) // message joker
        {
            if(viewModel.code.value.equals(""))
                startActivity(getShareIntentText(viewModel.question.value, viewModel.answer.value, viewModel.type.value))
            else
                startActivity(getShareIntentText(viewModel.question.value + "\n" + viewModel.code.value + "\n"
                    , viewModel.answer.value, viewModel.type.value))
        }
        else
            startActivity(getShareIntentCall())
        viewModel.usedJoker()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.joker_menu, menu)
        // check if the activity resolves
        if (null == getShareIntentText(viewModel.question.value + "\n" + viewModel.code.value, viewModel.answer.value, viewModel.type.value)
                .resolveActivity(requireActivity().packageManager) ||
            null == getShareIntentCall().resolveActivity(requireActivity().packageManager)) {
            menu.findItem(R.id.joker)?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(!viewModel.jokerUsed) {
            val dialogClickListener =
                DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            shareSuccess(0)
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                            shareSuccess(1)
                        }
                    }
                }

            val builder: AlertDialog.Builder = AlertDialog.Builder(context)

            when (item.itemId) {
                R.id.joker -> builder.setMessage(getString(R.string.joker_choose))
                    .setPositiveButton(getString(R.string.message_option), dialogClickListener)
                    .setNegativeButton(getString(R.string.call_option), dialogClickListener).show()
            }
        }
        else {
            when (item.itemId) {
                R.id.joker -> Toast.makeText(activity, getString(R.string.joker_used), Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}