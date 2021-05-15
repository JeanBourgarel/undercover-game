package com.example.myundercover

import java.util.Collections.shuffle

class Game(val nbPlayers: Int) {
    var players: MutableList<Player> = mutableListOf()
    lateinit var winner: Role
    lateinit var secretWord: String
    lateinit var fakeWord: String
    var roles: MutableList<Role> = mutableListOf()

    init {
        setRoles()
    }


    fun addPlayer(name: String, role: Role) {
        val newPlayer = Player(name, role)
        players.add(newPlayer)

    }

    fun killPlayer(player: Player) {

    }

    fun setWords() {
        secretWord = "Dog"
        fakeWord = "Cat"
    }

    private fun setRoles() {
        val nbInnocent = nbPlayers - 2
        repeat(nbInnocent) {
            roles.add(Innocent)
        }
        roles.add(MrWhite)
        roles.add(Undercover)
        shuffle(roles)
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