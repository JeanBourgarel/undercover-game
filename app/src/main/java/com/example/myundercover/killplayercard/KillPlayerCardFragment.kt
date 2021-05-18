package com.example.myundercover.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.myundercover.adapters.PlayerCardHolder
import com.example.myundercover.databinding.FragmentKillPlayerCardBinding
import io.uniflow.android.AndroidDataFlow
import org.koin.android.ext.android.inject

class KillPlayerCardViewModel : AndroidDataFlow() {

}

class KillPlayerCardFragment(val listener: IKillPlayer) : DialogFragment() {

    val KillPlayerCardViewModel: KillPlayerCardViewModel by inject()
    lateinit var binding: FragmentKillPlayerCardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentKillPlayerCardBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cardHolder = arguments?.getSerializable("cardHolder") as PlayerCardHolder;

        binding.killButton.setOnClickListener {
            listener.killPlayer(cardHolder)
            dismiss()
        }
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    interface IKillPlayer {
        fun killPlayer(cardHolder: PlayerCardHolder)
    }

}