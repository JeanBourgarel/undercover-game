package com.example.myundercover.fragments

import android.content.Intent
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
import com.example.myundercover.databinding.FragmentGameBinding
import io.uniflow.android.AndroidDataFlow
import io.uniflow.android.livedata.onEvents
import io.uniflow.android.livedata.onStates
import io.uniflow.core.flow.data.UIEvent
import io.uniflow.core.flow.data.UIState
import org.koin.android.ext.android.inject
import androidx.navigation.fragment.findNavController
import com.example.myundercover.fragments.HomeFragmentDirections
import com.example.myundercover.databinding.FragmentHomeBinding
import org.koin.android.ext.android.inject
import splitties.views.onClick


sealed class GameState : UIState()
object Started : GameState()
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

    fun startGame(game: Game) {
        action {
            setState(Started)
        }
    }
}

class GameFragment : Fragment(), PlayerCardAdapter.ICardRecycler, SecretWordFragment.ISecretWord, KillPlayerCardFragment.IKillPlayer {

    val GameViewModel: GameViewModel by inject()
    private val args by navArgs<GameFragmentArgs>()
    lateinit var binding: FragmentGameBinding
    lateinit var game: Game
    val cardDialog = CardFragment(this)
    val killPlayerCardDialog = KillPlayerCardFragment(this)
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
                    Toast.makeText(context, "Game can start", Toast.LENGTH_SHORT).show()
                }
                is Ended -> {
                    when (state.winnerRole) {
                        is Innocent -> {
                            Toast.makeText(context, "The innocents has won the game", Toast.LENGTH_SHORT).show()
                        }
                        is Undercover -> {
                            Toast.makeText(context, "The infiltrates has won the game", Toast.LENGTH_SHORT).show()
                        }
                        is MrWhite -> {
                            Toast.makeText(context, "The infiltrates has won the game", Toast.LENGTH_SHORT).show()
                        }
                    }
                    findNavController().navigate(GameFragmentDirections.launchHomeFragment())
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
            GameViewModel.startGame(game)
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