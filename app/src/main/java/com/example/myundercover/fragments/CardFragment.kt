package com.example.myundercover.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.myundercover.Game
import com.example.myundercover.databinding.FragmentCardBinding
import io.uniflow.android.AndroidDataFlow
import org.koin.android.ext.android.inject

class CardViewModel: AndroidDataFlow() {

}

class CardFragment: DialogFragment() {

    val CardViewModel: CardViewModel by inject()
    lateinit var binding: FragmentCardBinding

    companion object {
        fun newInstance(secretWord: String, fakeWord: String): DialogFragment {
            val f = DialogFragment()

            // Supply num input as an argument.
            val args = Bundle()
            args.putString("secretWord", secretWord)
            args.putString("fakeWord", fakeWord)
            f.setArguments(args)
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCardBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fakeWord = getArguments()?.getString("fakeWord");
        val game = getArguments()?.getSerializable("game") as Game;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.getSecretWordButton.setOnClickListener {
            val dialog = SecretWordFragment()
            dialog.show(childFragmentManager, "secretWordFragment")
        }
    }
}