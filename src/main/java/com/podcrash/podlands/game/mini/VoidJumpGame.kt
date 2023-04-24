package com.podcrash.podlands.game.mini

import com.podcrash.podlands.game.GameManager
import com.podcrash.podlands.game.Minigame
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class VoidJumpGame(gm: GameManager) : Minigame(gm) {

    override val name = "Jump!"
    override val duration = 5
    override val instructions = "Jump Off The Platform"

    override val listener = object: Listener {
        private val winHeight = gm.getMap().offPlatformHeight()

        @EventHandler
        fun onPlayerMove(event: PlayerMoveEvent) {
            val p = event.player

            if (pm.isPlayer(p) && p.location.y < winHeight ) {
                pm.winRoundAndTeleport(p, map)
            }
        }
    }
}