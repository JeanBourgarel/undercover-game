package com.example.myundercover

import com.example.myundercover.game.Game
import junit.framework.TestCase

class GameTest : TestCase() {

    fun testSetRolesLength() {
        val game: Game = Game(8)
        assertEquals(game.roles.size, 8)
    }

    fun testAddPlayer() {
        val game: Game = Game(8)
        game.addPlayer("p1")
        game.addPlayer("p2")
        game.addPlayer("p3")
        assertEquals(game.players.size, 3)
    }

    fun testNbInnocent() {
        val game: Game = Game(5)
        var nbInnocent = 0
        game.addPlayer("p1")
        game.addPlayer("p2")
        game.addPlayer("p3")
        game.addPlayer("p4")
        game.addPlayer("p5")
        for (player in game.players) {
            if (player.role === Innocent) {
                nbInnocent++
            }
        }
        println(nbInnocent)
        assertEquals(nbInnocent, 3)
    }

    fun testKillPlayer() {
        val game: Game = Game(5)
        var isDead = false
        game.addPlayer("p1")
        game.addPlayer("p2")
        game.addPlayer("p3")
        game.addPlayer("p4")
        game.addPlayer("p5")
        game.killPlayerByName("p3")
        for (player in game.players) {
            if (player.name === "p3" && !player.alive) {
                isDead = true
            }
        }
        assert(isDead)
    }

    fun testIsAliveOnDead() {
        val game: Game = Game(5)
        var isAlive = false
        game.addPlayer("p1")
        game.addPlayer("p2")
        game.addPlayer("p3")
        game.addPlayer("p4")
        game.addPlayer("p5")
        game.killPlayerByName("p3")
        isAlive = game.isPlayerAlive("p3")
        assert(!isAlive)
    }
    fun testIsAliveOnAlive() {
        val game: Game = Game(5)
        var isAlive = false
        game.addPlayer("p1")
        game.addPlayer("p2")
        game.addPlayer("p3")
        game.addPlayer("p4")
        game.addPlayer("p5")
        game.killPlayerByName("p3")
        isAlive = game.isPlayerAlive("p5")
        assert(isAlive)
    }

    fun testGetWinnerNoOne() {
        val game: Game = Game(5)
        var winner : Role? = null
        game.addPlayer("p1")
        game.addPlayer("p2")
        game.addPlayer("p3")
        game.addPlayer("p4")
        game.addPlayer("p5")
        winner = game.getWinner()
        assertEquals(winner, null)
    }

    fun testGetWinnerInfiltrate() {
        val game: Game = Game(5)
        var winner : Role? = null
        game.addPlayer("p1")
        game.addPlayer("p2")
        game.addPlayer("p3")
        game.addPlayer("p4")
        game.addPlayer("p5")
        for (player in game.players) {
            if (player.role == Innocent) {
                game.killPlayerByName(player.name)
            }
        }
        winner = game.getWinner()
        assertEquals(winner, MrWhite)
    }

    fun testGetWinnerInnocent() {
        val game: Game = Game(5)
        var winner : Role? = null
        game.addPlayer("p1")
        game.addPlayer("p2")
        game.addPlayer("p3")
        game.addPlayer("p4")
        game.addPlayer("p5")
        for (player in game.players) {
            if (player.role == MrWhite || player.role == Undercover) {
                game.killPlayerByName(player.name)
            }
        }
        winner = game.getWinner()
        assertEquals(winner, Innocent)
    }
}