package com.podcrash.podlands.game.mini

import com.podcrash.podlands.game.GameManager
import com.podcrash.podlands.game.Minigame
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class LightTNTGame(gm: GameManager) : Minigame(gm) {

    override val name = "Light TNT"
    override val duration = 8
    override val instructions = "Quickly set off a block of TNT"

    override val listener = object: Listener {
        @EventHandler
        fun onPlayerUse(e: PlayerInteractEvent) {
            val p = e.player
            if (pm.isPlayer(p)) {
                if (e.action == Action.RIGHT_CLICK_BLOCK && e.clickedBlock?.type == Material.TNT && e.item?.type == Material.FLINT_AND_STEEL ) {
                    pm.winRound(p)
                    e.clickedBlock!!.type = Material.AIR
                }
            }
        }

        @EventHandler
        fun onTNTExplode(e: EntityExplodeEvent) {
            if (e.entity.type == EntityType.PRIMED_TNT) {
                e.yield = 0f
                //e.isCancelled = true
                // TODO: maybe do something cool in here with removing some of the platform
            }
        }
    }

    override fun start() {
        super.start()
        pm.setInventory(ItemStack(Material.FLINT_AND_STEEL, 1))
        map.spawnNBlocks(pm.count(), Material.TNT)
    }

    override fun end() {
        super.end()
        map.despawnMob(EntityType.PRIMED_TNT)
    }

}