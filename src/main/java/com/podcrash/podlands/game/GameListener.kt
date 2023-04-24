package com.podcrash.podlands.game

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class GameListener(val gm: GameManager): Listener {

    private val pm = gm.playerManager
    private val offPlatform = gm.getMap().offPlatformHeight()

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val p = event.player

        if (pm.isPlayer(p) && p.location.y < offPlatform ) {

        }
    }


}