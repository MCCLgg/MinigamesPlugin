package com.podcrash.podlands.game

import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Listener

abstract class Minigame(val gm: GameManager) {
    abstract val name: String
    abstract val duration: Int
    abstract val instructions: String

    abstract val listener: Listener

    var failList: MutableList<Player> = mutableListOf()

    abstract val addPointsAtEnd: Boolean

    open fun start() {
        failList.clear()

        gm.announceMsg("Round ${gm.round}")
        gm.announceMsg(name)
        gm.announceSound(Sound.ENTITY_PLAYER_LEVELUP, 1.5f)
    }

    open fun end() {
        gm.clearInventory()
        gm.announceSound(Sound.ENTITY_PLAYER_LEVELUP, 0.5f)

        if (addPointsAtEnd) {
            gm.winnersPoint(failList)
        }
    }

}