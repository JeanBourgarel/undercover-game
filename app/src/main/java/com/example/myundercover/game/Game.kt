package com.example.myundercover.game

import android.content.Context
import java.io.Serializable
import java.util.Collections.shuffle
import com.example.myundercover.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Game(val nbPlayers: Int): Serializable {
    var players: MutableList<Player> = mutableListOf()
    lateinit var secretWord: String
    lateinit var fakeWord: String
    var roles: MutableList<Role> = mutableListOf()

    init {
        setRoles()
    }

    fun isPlayerAlive(playerName: String): Boolean {
        for (player in players) {
            if (player.name == playerName) {
                return player.alive
            }
        }
        return true
    }

    fun getFirstPlayerTurn(): Player? {
        for (player in players) {
            if (player.role == Innocent || player.role == Undercover) {
                return player
            }
        }
        return null
    }

    fun getRoleByName(playerName: String): Role? {
        for (player in players) {
            if (player.name == playerName) {
                return player.role
            }
        }
        return null
    }

    fun addPlayer(name: String) {
        val role = roles[0]
        val newPlayer = Player(name, role)
        players.add(newPlayer)
        roles.removeFirst()
    }

    fun killPlayerByName(playerName: String) {
        for (player in players) {
            if (player.name == playerName) {
                player.alive = false
                break
            }
        }
    }

    fun setWords(context: Context) {
        val jsonFileString = getJsonDataFromAsset(context, "secret_words.json")
        val listWordsToGuessType = object : TypeToken<List<WordToGuess>>() {}.type
        val wordsToGuess: MutableList<WordToGuess> = Gson().fromJson(jsonFileString, listWordsToGuessType)

        shuffle(wordsToGuess)
        secretWord = wordsToGuess[0].secretWord
        fakeWord = wordsToGuess[0].fakeWord
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

    fun getWinner() : Role? {
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
            return Innocent
        } else if (mrWhiteAlive && nbInnocentAlive <= 1) {
            return MrWhite
        } else if (undercoverAlive && nbInnocentAlive <= 1) {
            return MrWhite
        }
        return null
    }
}