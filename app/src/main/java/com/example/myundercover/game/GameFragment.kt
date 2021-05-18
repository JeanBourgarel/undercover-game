package com.example.myundercover.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myundercover.*
import com.example.myundercover.adapters.PlayerCardAdapter
import com.example.myundercover.adapters.PlayerCardHolder
import com.example.myundercover.card.PlayerCard
import com.example.myundercover.databinding.FragmentGameBinding
import com.example.myundercover.fragments.*
import io.uniflow.android.AndroidDataFlow
import io.uniflow.android.livedata.onEvents
import io.uniflow.android.livedata.onStates
import io.uniflow.core.flow.data.UIEvent
import io.uniflow.core.flow.data.UIState
import org.koin.android.ext.android.inject

sealed class GameState : UIState()
data class Started(val firstPlayer: Player) : GameState()
data class CardSelection(val playerNb: Int) : GameState()
data class Ended(val winnerRole: Role) : GameState()

sealed class GameEvent : UIEvent() {
    data class SelectNewCard(val holder: PlayerCardHolder) : GameEvent()
    data class SelectPlayerCard(val holder: PlayerCardHolder) : GameEvent()
    data class PlayerKilled(val holder: PlayerCardHolder, val role: Role) : GameEvent()
    data class PlayerAdded(val holder: PlayerCardHolder, val playerName: String) : GameEvent()
}

class GameViewModel : AndroidDataFlow() {

    init {
        action {
            setState(CardSelection(1))
        }
    }

    fun clickOnCard(holder: PlayerCardHolder, game: Game) = action { currentState ->
        when (currentState) {
            is CardSelection -> {
                if (holder.name.text.isBlank()) {
                    sendEvent(GameEvent.SelectNewCard(holder))
                }
            }
            is Started -> {
                if (game.isPlayerAlive(holder.name.text.toString())) {
                    sendEvent(GameEvent.SelectPlayerCard(holder))
                }
            }
        }
    }

    fun playerAdded(holder: PlayerCardHolder, playerName: String) {
        action {
            sendEvent(GameEvent.PlayerAdded(holder, playerName))
        }
    }

    fun killPlayer(holder: PlayerCardHolder, role: Role) {
        action {
            sendEvent(GameEvent.PlayerKilled(holder, role))
        }
    }

    fun endGame(winner: Role) {
        action {
            setState(Ended(winner))
        }
    }

    fun selectCards(playerNb: Int) {
        action {
            setState(CardSelection(playerNb))
        }
    }

    fun startGame(firstPlayer: Player) {
        action {
            setState(Started(firstPlayer))
        }
    }
}

class GameFragment : Fragment(), PlayerCardAdapter.ICardRecycler, SecretWordFragment.ISecretWord,
    KillPlayerCardFragment.IKillPlayer {

    private val GameViewModel: GameViewModel by inject()
    private val args by navArgs<GameFragmentArgs>()
    private lateinit var binding: FragmentGameBinding
    private lateinit var game: Game
    private val cardDialog = CardFragment(this)
    private val killPlayerCardDialog = KillPlayerCardFragment(this)
    private var playerNb = 0
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
        game.setWords(requireContext())
        onStates(GameViewModel) { state ->
            when (state) {
                is CardSelection -> {
                    val newText = getString(R.string.player) + " " + state.playerNb + " " + getString(R.string.choose_a_card)
                    binding.nbPlayer.text = newText
                }
                is Started -> {
                    binding.nbPlayer.text = getString(R.string.game_has_started, state.firstPlayer.name)
                    Toast.makeText(context, getString(R.string.game_has_started, state.firstPlayer.name), Toast.LENGTH_SHORT).show()
                }
                is Ended -> {
                    findNavController().navigate(
                        GameFragmentDirections.launchEndScreenFragment(
                            state.winnerRole
                        )
                    )
                }
            }
        }
        onEvents(GameViewModel) { event ->
            when (event) {
                is GameEvent.SelectNewCard -> {
                    val args = Bundle()
                    args.putSerializable("cardHolder", event.holder)
                    args.putSerializable("game", game)
                    cardDialog.arguments = args
                    cardDialog.show(childFragmentManager, "cardFragment")
                }
                is GameEvent.SelectPlayerCard -> {
                    val args = Bundle()
                    args.putSerializable("cardHolder", event.holder)
                    args.putSerializable("game", game)
                    killPlayerCardDialog.arguments = args
                    killPlayerCardDialog.show(childFragmentManager, "killPlayerCardFragment")
                }
                is GameEvent.PlayerAdded -> {
                    event.holder.name.text = event.playerName
                }
                is GameEvent.PlayerKilled -> {
                    event.holder.icon.setImageResource(R.mipmap.ic_dead)
                    when (event.role) {
                        is Innocent -> {
                            Toast.makeText(context, "An innocent has been killed", Toast.LENGTH_SHORT).show()
                        }
                        is Undercover -> {
                            Toast.makeText(context, "The undercover has been killed", Toast.LENGTH_SHORT).show()
                        }
                        is MrWhite -> {
                            Toast.makeText(context, "The Mr White has been killed", Toast.LENGTH_SHORT).show()
                        }
                    }
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
        GameViewModel.clickOnCard(holder, game)
    }

    override fun clickOnOk(updatedGame: Game, cardHolder: PlayerCardHolder, playerName: String) {
        GameViewModel.playerAdded(cardHolder, playerName)
        game = updatedGame
        if (game.roles.size == 0) {
            val firstPlayer = game.getFirstPlayerTurn()
            if (firstPlayer != null) {
                GameViewModel.startGame(firstPlayer)
            }
        } else {
            GameViewModel.selectCards(game.players.size + 1)
        }
        cardDialog.dismiss()
    }

    override fun killPlayer(cardHolder: PlayerCardHolder) {
        val role = game.getRoleByName(cardHolder.name.text.toString())
        game.killPlayerByName(cardHolder.name.text.toString())
        if (role != null) {
            GameViewModel.killPlayer(cardHolder, role)
        }
        val winner = game.getWinner()
        if (winner != null) {
            GameViewModel.endGame(winner)
        }
    }
}