package com.example.myundercover

import java.io.Serializable

sealed class Role: Serializable
object Innocent: Role()
object MrWhite: Role()
object Undercover: Role()

class Player(var name: String, var role: Role) {
    var alive: Boolean = true
}