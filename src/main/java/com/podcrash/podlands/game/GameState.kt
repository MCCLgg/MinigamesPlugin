package com.podcrash.podlands.game

sealed class GameState {
    abstract fun onEnter()
    abstract fun onLeave()

    object Lobby: GameState() {
        override fun onEnter() {

        }

        override fun onLeave() {

        }
    }

    object Game: GameState() {
        override fun onEnter() {

        }

        override fun onLeave() {

        }
    }
}

