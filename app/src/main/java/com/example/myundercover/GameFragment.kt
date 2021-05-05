package com.example.myundercover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.myundercover.databinding.FragmentGameBinding
import io.uniflow.android.AndroidDataFlow
import io.uniflow.android.livedata.onStates
import io.uniflow.core.flow.data.UIState
import kotlinx.coroutines.delay
import org.koin.android.ext.android.inject

sealed class GameStep: UIState()
object SelectCard: GameStep()
data class End(val winnerRole: Role)
data class ShowCard(val player: Player): GameStep()
data class NewTurn(val playerTurn: Player): GameStep()
data class killPlayer(val player: Player): GameStep()


sealed class GameState: UIState()
object Idle: GameState()
object Loading: GameState()
data class Result(val data: String): GameState()

class GameViewModel: AndroidDataFlow() {

    init {
        action {
            setState(SelectCard)
        }
    }

    fun clickOnCard(player: Player) {
        action {
            setState(ShowCard(player))
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

    private val args by navArgs<GameFragmentArgs>()
    lateinit var binding: FragmentGameBinding
    lateinit var game: Game

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGameBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*        onStates(GameViewModel) { state ->
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
        }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nbPlayer.text = "There is ${args.nbPlayers} players."
    }
}