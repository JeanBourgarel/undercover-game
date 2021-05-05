package com.example.myundercover.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myundercover.databinding.FragmentCardBinding
import io.uniflow.android.AndroidDataFlow

class CardViewModel: AndroidDataFlow() {

}

class CardFragment: Fragment() {

    //    val CardViewModel: CardViewModel by inject()
    lateinit var binding: FragmentCardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCardBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}