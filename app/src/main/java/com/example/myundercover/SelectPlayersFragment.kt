package com.example.myundercover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.myundercover.databinding.FragmentSelectPlayersBinding
import io.uniflow.android.AndroidDataFlow
import org.koin.android.ext.android.inject
import splitties.toast.toast

class SelectPlayersViewModel: AndroidDataFlow() {

}

class SelectPlayersFragment: Fragment() {

//    val SelectPlayersViewModel: SelectPlayersViewModel by inject()
    private val args by navArgs<SelectPlayersFragmentArgs>()
    lateinit var binding: FragmentSelectPlayersBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSelectPlayersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dataDisplay.text = args.sampleData
    }
}