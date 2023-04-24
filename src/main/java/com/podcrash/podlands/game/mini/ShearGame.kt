package com.podcrash.podlands.game.mini

import com.podcrash.podlands.game.GameManager
import com.podcrash.podlands.game.Minigame
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.PlayerShearEntityEvent
import org.bukkit.inventory.ItemStack

class ShearGame(gm: GameManager) : Minigame(gm) {

    override val name = "Sheared"
    override val duration = 8
    override val instructions = "Shear a sheep"

    override val listener = object: Listener {
        @EventHandler
        fun onPlayerSheared(event: PlayerShearEntityEvent) {
            val p = event.player
            if (pm.isPlayer(p)) {
                event.isCancelled = true
                event.entity.remove()
                pm.winRound(p)
            }
        }
    }

    override fun start() {
        super.start()

        val shears = ItemStack(Material.SHEARS, 1)
        pm.setInventory(shears)
        map.spawnNMobs(pm.count(), EntityType.SHEEP)
    }

    override fun end() {
        super.end()
        map.despawnMob(EntityType.SHEEP)
    }

}