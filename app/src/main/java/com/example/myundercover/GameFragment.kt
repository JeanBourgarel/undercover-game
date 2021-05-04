package com.example.myundercover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myundercover.databinding.FragmentGameBinding
import io.uniflow.android.AndroidDataFlow
import io.uniflow.android.livedata.onStates
import io.uniflow.core.flow.data.UIState
import kotlinx.coroutines.delay
import org.koin.android.ext.android.inject
import splitties.views.onClick

sealed class GameState: UIState()
object Idle: GameState()
object Loading: GameState()
data class Result(val data: String): GameState()


class GameViewModel: AndroidDataFlow() {

    init {
        action {
            setState(Idle)
        }
    }

    fun click() {
        action {
            setState(Loading)
            delay(3000)
            setState(Result("Hello world"))
        }
    }
}

class GameFragment: Fragment() {

    val GameViewModel: GameViewModel by inject()

    lateinit var binding: FragmentGameBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGameBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onStates(GameViewModel) { state ->
            when (state) {
                is Idle -> {
                    binding.counter.isVisible = false
                    binding.loader.isVisible = false
                    binding.clickMe.isVisible = true
                }
                is Loading -> {
                    binding.counter.isVisible = false
                    binding.loader.isVisible = true
                    binding.clickMe.isVisible = false
                }
                is Result -> {
                    binding.counter.isVisible = true
                    binding.loader.isVisible = false
                    binding.clickMe.isVisible = false

                    binding.counter.text = state.data

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clickMe.onClick {
            GameViewModel.click()
        }
        binding.goToNextPage.onClick {
            findNavController().navigate(GameFragmentDirections.launchSelectPlayersFragment("Salut les moches"))
        }
    }
}