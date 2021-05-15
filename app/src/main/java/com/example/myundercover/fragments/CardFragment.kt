package com.example.myundercover.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myundercover.databinding.FragmentCardBinding
import io.uniflow.android.AndroidDataFlow
import org.koin.android.ext.android.inject
import splitties.views.onClick

class CardViewModel: AndroidDataFlow() {

}

class CardFragment: DialogFragment() {

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
    }
}