package com.podcrash.podlands.game

import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Listener

abstract class Minigame(val gm: GameManager, private val addPointsAtEnd: Boolean = false) {
    abstract val name: String
    abstract val duration: Int
    abstract val instructions: String

    abstract val listener: Listener

    // TODO: Maybe don't need this list? (Used mainly for 'last' games where points are added at end)
    var failList: MutableList<Player> = mutableListOf()

    protected var pm: PlayerManager = gm.playerManager
    protected var map: Map = gm.getMap()

    protected open var gamemode: GameMode = GameMode.ADVENTURE

    open fun start() {
        failList.clear()

        pm.clearInventory()
        pm.spreadPlayers(map)
        pm.gamemode(gamemode)
        pm.announce("Round ${gm.round}")
        pm.announce(name)
        pm.sound(Sound.ENTITY_PLAYER_LEVELUP, 0.8f)
    }

    open fun end() {
        pm.clearInventory()
        pm.sound(Sound.ENTITY_PLAYER_LEVELUP, 0.6f)

        if (addPointsAtEnd) {
            pm.winnersPoint(failList)
        }
    }

}