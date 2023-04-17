package com.podcrash.podlands.game.micro

import com.podcrash.podlands.game.GameManager
import com.podcrash.podlands.game.Minigame
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerShearEntityEvent
import org.bukkit.inventory.ItemStack

class BreakTorchGame(gm: GameManager) : Minigame(gm) {

    override val name = "Turn off the lights"
    override val duration = 6
    override val instructions = "Break any torch"

    override val addPointsAtEnd = false

    override val listener = BreakTorchListener(gm)

    override fun start() {
        super.start()
        gm.spawnNBlocks(gm.players.keys.size, Material.TORCH)
    }

}
class BreakTorchListener(private val gm: GameManager) : Listener {
    @EventHandler
    fun onPlayerUse(e: BlockBreakEvent) {
        val p = e.player
        if (gm.isPlayer(p)) {
            if (e.block.type == Material.TORCH) {
                e.isDropItems = false
                gm.winRound(p)
            } else {
                e.isCancelled = true
            }
        }
    }
}