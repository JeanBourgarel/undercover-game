package com.example.myundercover

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myundercover.adapters.PlayerCardAdapter
import com.example.myundercover.databinding.FragmentGameBinding
import io.uniflow.android.AndroidDataFlow
import io.uniflow.android.livedata.onStates
import io.uniflow.core.flow.data.UIState
import kotlinx.coroutines.delay
import org.koin.android.ext.android.inject

sealed class GameState: UIState()
object SelectCard: GameState()
data class End(val winnerRole: Role): GameState()
data class ShowCard(val player: Player): GameState()
data class NewTurn(val playerTurn: Player): GameState()
data class killPlayer(val player: Player): GameState()


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
}

class GameFragment: Fragment() {

    val GameViewModel: GameViewModel by inject()
    private val args by navArgs<GameFragmentArgs>()
    lateinit var binding: FragmentGameBinding
    lateinit var game: Game

    private var recyclerViewPlayerCards: RecyclerView? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var arrayListPlayerCard: ArrayList<PlayerCard>? = null
    private var playerCardAdapter: PlayerCardAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGameBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onStates(GameViewModel) { state ->
            when (state) {
                is SelectCard -> {
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewPlayerCards = binding.recyclerViewCards
        gridLayoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        recyclerViewPlayerCards?.layoutManager = gridLayoutManager
        recyclerViewPlayerCards?.setHasFixedSize(true)
        arrayListPlayerCard = ArrayList()
        repeat(args.nbPlayers) {
            arrayListPlayerCard?.add(PlayerCard(R.mipmap.ic_unknown_face, "player name"))
        }
        playerCardAdapter = PlayerCardAdapter(arrayListPlayerCard!!, requireContext())
        recyclerViewPlayerCards?.adapter = playerCardAdapter
    }
}