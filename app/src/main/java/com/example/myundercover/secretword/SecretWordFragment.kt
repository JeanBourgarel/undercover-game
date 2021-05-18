package com.example.myundercover.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.myundercover.*
import com.example.myundercover.adapters.PlayerCardHolder
import com.example.myundercover.databinding.FragmentSecretWordBinding
import com.example.myundercover.game.Game
import io.uniflow.android.AndroidDataFlow
import org.koin.android.ext.android.inject

class SecretWordViewModel: AndroidDataFlow() {

}

class SecretWordFragment(val listener: ISecretWord): DialogFragment() {

    val SecretWordViewModel: SecretWordViewModel by inject()
    lateinit var binding: FragmentSecretWordBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSecretWordBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val game = arguments?.getSerializable("game") as Game;
        val cardHolder = arguments?.getSerializable("cardHolder") as PlayerCardHolder;
        val playerName = arguments?.getString("playerName") as String;

        when (game.players.last().role) {
            is Innocent -> {
                binding.secretWord.text = game.secretWord
            }
            is MrWhite -> {
                binding.yourSecretWordIsTextView.text = getString(R.string.you_are_the_mr_white)
                binding.secretWord.text = getString(R.string.dont_get_caught)
            }
            is Undercover -> {
                binding.secretWord.text = game.fakeWord
            }
        }
        binding.okButton.setOnClickListener {
            listener.clickOnOk(game, cardHolder, playerName)
            dismiss()
        }
    }

    interface ISecretWord {
        fun clickOnOk(updatedGame: Game, cardHolder: PlayerCardHolder, playerName: String)
    }
}