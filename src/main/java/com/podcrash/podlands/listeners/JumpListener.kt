package com.podcrash.podlands.listeners

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class JumpListener : Listener {

    @EventHandler
    fun playerJumpEvent(e: PlayerJumpEvent) {
        val p = e.player
        p.sendMessage("You jumped!")
    }

}