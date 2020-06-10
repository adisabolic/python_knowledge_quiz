package com.example.projekat_kviz.screens.score

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil.inflate
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.projekat_kviz.R
import com.example.projekat_kviz.databinding.ScoreFragmentBinding

class ScoreFragment : Fragment() {

    private lateinit var viewModel: ScoreViewModel
    private lateinit var viewModelFactory: ScoreViewModelFactory
    private lateinit var binding: ScoreFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, R.layout.score_fragment, container, false)

        viewModelFactory = ScoreViewModelFactory(
            ScoreFragmentArgs.fromBundle(requireArguments()).score,
            ScoreFragmentArgs.fromBundle(requireArguments()).nmbrQuestion,
            ScoreFragmentArgs.fromBundle(requireArguments()).jokerUsed,
            ScoreFragmentArgs.fromBundle(requireArguments()).difficulty
        )

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ScoreViewModel::class.java)
        binding.scoreViewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.eventPlayAgain.observe(viewLifecycleOwner, Observer { playAgain ->
            if (playAgain) {
                findNavController().navigate(ScoreFragmentDirections.actionRestart())
                viewModel.onPlayAgainComplete()
            }
        })

        setScore()
        setJoker()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun getShareIntent(): Intent {
        val args = ScoreFragmentArgs.fromBundle(requireArguments())
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
            .putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.share_success_text, args.score, args.nmbrQuestion, args.difficulty)
            )
        return shareIntent
    }

    private fun shareSuccess() {
        startActivity(getShareIntent())
    }

    private fun setScore() {
        if(viewModel.nmbrQuestion.value == viewModel.score.value)
            binding.youScoredText.text = getString(R.string.you_scored_max)
        else
            binding.youScoredText.text = getString(R.string.you_scored)
    }

    private fun setJoker() {
        if(viewModel.jokerUsed)
            binding.jokerText.text = getString(R.string.joker_yes)
        else
            binding.jokerText.text = getString(R.string.joker_no)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share_success_menu, menu)
        // check if the activity resolves
        if (null == getShareIntent().resolveActivity(requireActivity().packageManager)) {
            // hide the menu item if it doesn't resolve
            menu.findItem(R.id.share)?.isVisible = false
        }
    }

    // Sharing from the Menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }
}