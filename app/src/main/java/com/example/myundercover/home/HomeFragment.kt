package com.example.myundercover.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myundercover.fragments.HomeFragmentDirections
import com.example.myundercover.databinding.FragmentHomeBinding
import io.uniflow.android.AndroidDataFlow
import org.koin.android.ext.android.inject
import splitties.views.onClick

class HomeViewModel: AndroidDataFlow() {

}

class HomeFragment: Fragment() {

    val HomeViewModel: HomeViewModel by inject()
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newGame.onClick {
            findNavController().navigate(HomeFragmentDirections.launchSelectPlayersFragment())
        }
    }
}