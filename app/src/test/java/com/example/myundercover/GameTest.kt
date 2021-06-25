package com.example.myundercover

import com.example.myundercover.game.Game
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat

class GameTest : TestCase() {

    fun testFirstPlayer() {
        val game: Game = Game(5)
        game.addPlayer("p1")
        game.addPlayer("p2")
        game.addPlayer("p3")
        game.addPlayer("p4")
        game.addPlayer("p5")
        val firstPlayer = game.getFirstPlayerTurn()
        assertThat(firstPlayer, instanceOf(Player::class.java))
    }

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

}