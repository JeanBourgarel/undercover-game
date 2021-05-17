package com.example.myundercover.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myundercover.*
import com.example.myundercover.adapters.PlayerCardAdapter
import com.example.myundercover.adapters.PlayerCardHolder
import com.example.myundercover.databinding.FragmentGameBinding
import io.uniflow.android.AndroidDataFlow
import io.uniflow.android.livedata.onEvents
import io.uniflow.android.livedata.onStates
import io.uniflow.core.flow.data.UIEvent
import io.uniflow.core.flow.data.UIState
import org.koin.android.ext.android.inject

sealed class GameState : UIState()
object Started : GameState()
data class CardSelection(val playerNb: Int) : GameState()
data class End(val winnerRole: Role) : GameState()
data class ShowCard(val player: Player) : GameState()
data class NewTurn(val playerTurn: Player) : GameState()
data class killPlayer(val player: Player) : GameState()


sealed class GameEvent : UIEvent() {
    data class SelectNewCard(val holder: PlayerCardHolder) : GameEvent()
    data class SelectPlayerCard(val holder: PlayerCardHolder) : GameEvent()
}

class GameViewModel : AndroidDataFlow() {

    init {
        action {
            setState(CardSelection(1))
        }
    }

    fun clickOnCard(holder: PlayerCardHolder) = action { currentState ->
        when (currentState) {
            is CardSelection -> {
                sendEvent(GameEvent.SelectNewCard(holder))
            }
            is Started -> {
                sendEvent(GameEvent.SelectPlayerCard(holder))
            }
        }
    }

    fun selectCards(playerNb: Int) {
        action {
            setState(CardSelection(playerNb))
        }
    }

    fun startGame(game: Game) {
        action {
            setState(Started)
        }
    }
}

class GameFragment : Fragment(), PlayerCardAdapter.ICardRecycler, SecretWordFragment.ISecretWord {

    val GameViewModel: GameViewModel by inject()
    private val args by navArgs<GameFragmentArgs>()
    lateinit var binding: FragmentGameBinding
    lateinit var game: Game
    val dialog = CardFragment(this)
    var playerNb = 0

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
        playerNb = args.nbPlayers
        game = Game(args.nbPlayers)
        onStates(GameViewModel) { state ->
            when (state) {
                is CardSelection -> {
                    val newText = getString(R.string.player) + " " + state.playerNb + " " + getString(R.string.choose_a_card)
                    binding.nbPlayer.text = newText
                }
                is Started -> {
                    binding.nbPlayer.text = getString(R.string.game_has_started)
                }
            }
        }
        onEvents(GameViewModel) { event ->
            when (event) {
                is GameEvent.SelectNewCard -> {
                    if (event.holder.name.text.isBlank()) {
                        val args = Bundle()
                        args.putSerializable("cardHolder", event.holder)
                        args.putSerializable("game", game)
                        dialog.arguments = args
                        dialog.show(childFragmentManager, "cardFragment")
                    }
                }
                is GameEvent.SelectPlayerCard -> {
                    Toast.makeText(context, "Kill " + event.holder.name.text.toString() + " ?", Toast.LENGTH_SHORT).show()
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
        repeat(playerNb) {
            arrayListPlayerCard?.add(PlayerCard(R.mipmap.ic_unknown_face, ""))
        }
        playerCardAdapter = PlayerCardAdapter(arrayListPlayerCard!!, requireContext(), this)
        recyclerViewPlayerCards?.adapter = playerCardAdapter
    }

    override fun clickOnCard(holder: PlayerCardHolder) {
        GameViewModel.clickOnCard(holder)
    }

    override fun clickOnOk(updatedGame: Game, cardHolder: PlayerCardHolder, playerName: String) {
        cardHolder.name.text = playerName
        game = updatedGame
        if (game.roles.size == 0) {
            GameViewModel.startGame(game)
            Toast.makeText(context, "Game can start", Toast.LENGTH_SHORT).show()
        } else {
            GameViewModel.selectCards(game.players.size + 1)
        }
        dialog.dismiss()
    }
}