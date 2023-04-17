package com.podcrash.podlands.game.micro

import com.podcrash.podlands.game.GameManager
import com.podcrash.podlands.game.Minigame
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerShearEntityEvent
import org.bukkit.inventory.ItemStack

class EatCakeGame(gm: GameManager) : Minigame(gm) {

    override val name = "Eat Cake"
    override val duration = 6
    override val instructions = "Eat a slice of cake"

    override val addPointsAtEnd = false

    override val listener = EatCakeListener(gm)

    override fun start() {
        super.start()
        gm.players.keys.forEach {
            it.foodLevel = 19;
        }
        gm.spawnNBlocks(gm.players.keys.size, Material.CAKE)
    }

}
class EatCakeListener(private val gm: GameManager) : Listener {
    @EventHandler
    fun onPlayerUse(e: PlayerInteractEvent) {
        val p = e.player
        if (gm.isPlayer(p)) {
            if (e.action == Action.RIGHT_CLICK_BLOCK && e.clickedBlock?.type == Material.CAKE) {
                e.isCancelled = true
                gm.winRound(p)
            }
        }
    }
}