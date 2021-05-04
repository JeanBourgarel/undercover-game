package com.example.myundercover

sealed class Role
object Innocent: Role()
object MrWhite: Role()
object Undercover: Role()

class Player(var name: String, var role: Role, var alive: Boolean) {
    init {
    }
}