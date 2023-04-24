package com.podcrash.podlands.game.mini

import com.podcrash.podlands.game.GameManager
import com.podcrash.podlands.game.Minigame
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerShearEntityEvent
import org.bukkit.inventory.ItemStack

class MilkGame(gm: GameManager) : Minigame(gm) {

    override val name = "Milkd"
    override val duration = 8
    override val instructions = "Milk a cow"

    override val listener = object: Listener {
        @EventHandler
        fun onPlayerMilk(event: PlayerInteractEntityEvent) {
            val p = event.player
            if (pm.isPlayer(p) && event.rightClicked.type == EntityType.COW && event.player.activeItem.type == Material.BUCKET) {
                event.isCancelled = true
                event.rightClicked.remove()
                pm.winRound(p)
            }
        }
    }

    override fun start() {
        super.start()

        val bucket = ItemStack(Material.BUCKET, 1)
        pm.setInventory(bucket)
        map.spawnNMobs(pm.count(), EntityType.COW)
    }

    override fun end() {
        super.end()
        map.despawnMob(EntityType.SHEEP)
    }

}