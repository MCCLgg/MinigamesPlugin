package com.podcrash.podlands.game.mini

import com.podcrash.podlands.game.GameManager
import com.podcrash.podlands.game.Minigame
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BreakTorchGame(gm: GameManager) : Minigame(gm) {

    override val name = "Turn off the lights"
    override val duration = 6
    override val instructions = "Break any torch"

    override var gamemode = GameMode.SURVIVAL

    override val listener = object: Listener {
        @EventHandler
        fun onPlayerUse(e: BlockBreakEvent) {
            val p = e.player
            if (pm.isPlayer(p)) {
                if (e.block.type == Material.TORCH) {
                    e.isDropItems = false
                    pm.winRound(p)
                } else {
                    e.isCancelled = true
                }
            }
        }
    }

    override fun start() {
        super.start()
        map.spawnNBlocks(pm.count(), Material.TORCH)
    }

}