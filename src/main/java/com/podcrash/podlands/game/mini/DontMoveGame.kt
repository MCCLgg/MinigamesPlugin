package com.podcrash.podlands.game.mini

import com.podcrash.podlands.game.GameManager
import com.podcrash.podlands.game.Minigame
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class DontMoveGame(gm: GameManager) : Minigame(gm, true) {

    override val name = "Don't Move"
    override val duration = 4
    override val instructions = ""

    override val listener = object: Listener {
        @EventHandler
        fun onPlayerMove(event: PlayerMoveEvent) {
            val p = event.player

            if (pm.isPlayer(p) && !failList.contains(p)) {
                if (event.hasExplicitlyChangedBlock()) {
                    p.playSound(p.location, Sound.BLOCK_NOTE_BLOCK_COW_BELL, 1f, 1.0f)
                    failList.add(p)
                }
            }

        }
    }
}