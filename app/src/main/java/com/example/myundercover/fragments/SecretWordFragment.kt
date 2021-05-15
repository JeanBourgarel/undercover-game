package com.example.myundercover.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myundercover.Game
import com.example.myundercover.adapters.PlayerCardAdapter
import com.example.myundercover.adapters.PlayerCardHolder
import com.example.myundercover.databinding.FragmentSecretWordBinding
import io.uniflow.android.AndroidDataFlow
import org.koin.android.ext.android.inject
import splitties.views.onClick

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
        val playerName = arguments?.getString("playerName") as String;

        binding.secretWord.text = game.secretWord
        binding.okButton.setOnClickListener {
            game.addPlayer(playerName)
            listener.clickOnOk(game)
            dismiss()
        }
    }

    interface ISecretWord {
        fun clickOnOk(updatedGame: Game)
    }
}