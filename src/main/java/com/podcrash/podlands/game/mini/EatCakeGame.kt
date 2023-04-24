package com.podcrash.podlands.game.mini

import com.podcrash.podlands.game.GameManager
import com.podcrash.podlands.game.Minigame
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class EatCakeGame(gm: GameManager) : Minigame(gm) {

    override val name = "Eat Cake"
    override val duration = 6
    override val instructions = "Eat a slice of cake"

    override val listener = object: Listener {
        @EventHandler
        fun onPlayerUse(e: PlayerInteractEvent) {
            val p = e.player
            if (pm.isPlayer(p)) {
                if (e.action == Action.RIGHT_CLICK_BLOCK && e.clickedBlock?.type == Material.CAKE) {
                    e.isCancelled = true
                    pm.winRound(p)
                }
            }
        }
    }

    override fun start() {
        super.start()
        pm.setFoodLevel(18)
        map.spawnNBlocks(pm.count(), Material.CAKE)
    }
}