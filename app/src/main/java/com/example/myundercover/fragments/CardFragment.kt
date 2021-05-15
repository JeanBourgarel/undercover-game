package com.example.myundercover.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myundercover.Game
import com.example.myundercover.adapters.PlayerCardAdapter
import com.example.myundercover.databinding.FragmentCardBinding
import io.uniflow.android.AndroidDataFlow
import org.koin.android.ext.android.inject

class CardViewModel: AndroidDataFlow() {

}

class CardFragment(val listener: SecretWordFragment.ISecretWord): DialogFragment() {

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
        val playerName = binding.nameEditText.text.toString()
        binding.getSecretWordButton.setOnClickListener {
            val dialog = SecretWordFragment(listener)
            val args = Bundle()
            args.putSerializable("game", game)
            args.putString("playerName", playerName)
            dialog.arguments = args
            dialog.show(childFragmentManager, "secretWordFragment")
        }
    }


    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        println("la bite")
    }
}