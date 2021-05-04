package com.example.myundercover

import io.uniflow.core.flow.data.UIState

sealed class GameStep
object SelectCard: GameStep()
data class End(val winnerRole: Role)
data class ShowCard(val player: Player): GameStep()
data class NewTurn(val playerTurn: Player): GameStep()
data class killPlayer(val player: Player): GameStep()

class Game(var players: List<Player>) {
    lateinit var winner: Role
    fun killPlayer(player: Player) {

    }

    fun setRoles() {
        val shuffledPlayers = players.shuffled()
        shuffledPlayers[0].role = MrWhite
        shuffledPlayers[1].role = Undercover
        players = shuffledPlayers
    }

    fun getWinner() {
        var mrWhiteAlive = false
        var undercoverAlive = false
        var nbInnocentAlive = 0
        for (player in players) {
            if (player.alive) {
                when (player.role) {
                    Innocent -> {
                        nbInnocentAlive++
                    }
                    MrWhite -> {
                        mrWhiteAlive = true
                    }
                    Undercover -> {
                        undercoverAlive = true
                    }
                }
            }
        }
        if (!mrWhiteAlive && !undercoverAlive) {
            winner = Innocent
        } else if (mrWhiteAlive && nbInnocentAlive <= 1) {
            winner = MrWhite
        } else if (undercoverAlive && nbInnocentAlive <= 1) {
            winner = MrWhite
        }

    }
}