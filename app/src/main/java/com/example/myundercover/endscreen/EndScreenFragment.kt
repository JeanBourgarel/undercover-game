package com.example.myundercover.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myundercover.*
import com.example.myundercover.adapters.PlayerCardHolder
import com.example.myundercover.databinding.FragmentEndScreenBinding
import io.uniflow.android.AndroidDataFlow
import org.koin.android.ext.android.inject
import splitties.views.onClick

class EndScreenViewModel : AndroidDataFlow() {

}

class EndScreenFragment() : Fragment() {

    val EndScreenViewModel: EndScreenViewModel by inject()
    lateinit var binding: FragmentEndScreenBinding
    private val args by navArgs<EndScreenFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEndScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (args.winner) {
            is Innocent -> {
                binding.winnerTextView.text = getString(R.string.the_innocents_has_won)
            }
            is Undercover -> {
                binding.winnerTextView.text = getString(R.string.the_infiltrates_has_won)
            }
            is MrWhite -> {
                binding.winnerTextView.text = getString(R.string.the_infiltrates_has_won)
            }
        }
        binding.newGame.onClick {
            findNavController().navigate(HomeFragmentDirections.launchSelectPlayersFragment())
        }
    }

}