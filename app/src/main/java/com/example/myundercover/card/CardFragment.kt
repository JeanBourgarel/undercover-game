package com.example.myundercover.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.myundercover.game.Game
import com.example.myundercover.adapters.PlayerCardHolder
import com.example.myundercover.databinding.FragmentCardBinding
import io.uniflow.android.AndroidDataFlow
import org.koin.android.ext.android.inject

class CardViewModel : AndroidDataFlow() {

}

class CardFragment(val listener: SecretWordFragment.ISecretWord) : DialogFragment() {

    val CardViewModel: CardViewModel by inject()
    lateinit var binding: FragmentCardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCardBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val game = arguments?.getSerializable("game") as Game;
        val cardHolder = arguments?.getSerializable("cardHolder") as PlayerCardHolder;
        binding.getSecretWordButton.setOnClickListener {
            var nameExists = false
            val playerName = binding.nameEditText.text.toString()
            for (player in game.players) {
                if (player.name == playerName) {
                    nameExists = true
                }
            }
            when {
                playerName.isEmpty() -> {
                    Toast.makeText(context, "Please choose a name", Toast.LENGTH_SHORT).show()
                }
                nameExists -> {
                    Toast.makeText(context, "This name is already taken", Toast.LENGTH_SHORT).show()

                }
                else -> {
                    val dialog = SecretWordFragment(listener)
                    val args = Bundle()
                    game.addPlayer(playerName)
                    args.putSerializable("game", game)
                    args.putSerializable("cardHolder", cardHolder)
                    args.putString("playerName", playerName)
                    dialog.arguments = args
                    dialog.show(childFragmentManager, "secretWordFragment")
                    binding.nameEditText.text.clear()
                }
            }
        }
    }
}