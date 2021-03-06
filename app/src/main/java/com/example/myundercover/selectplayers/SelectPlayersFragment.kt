package com.example.myundercover.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myundercover.fragments.SelectPlayersFragmentDirections
import com.example.myundercover.databinding.FragmentSelectPlayersBinding
import io.uniflow.android.AndroidDataFlow
import org.koin.android.ext.android.inject
import splitties.views.onClick

class SelectPlayersViewModel: AndroidDataFlow() {

}

class SelectPlayersFragment: Fragment() {

    val SelectPlayersViewModel: SelectPlayersViewModel by inject()
    lateinit var binding: FragmentSelectPlayersBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSelectPlayersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.numberPicker.maxValue = 15
        binding.numberPicker.minValue = 5
        binding.startGame.onClick {
            findNavController().navigate(SelectPlayersFragmentDirections.launchGameFragment(binding.numberPicker.value))
        }
    }
}