package com.podcrash.podlands.game.micro

import com.podcrash.podlands.game.GameManager
import com.podcrash.podlands.game.Minigame
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerShearEntityEvent
import org.bukkit.inventory.ItemStack

class LightTNTGame(gm: GameManager) : Minigame(gm) {

    override val name = "Light TNT"
    override val duration = 8
    override val instructions = "Quickly set off a block of TNT"

    override val addPointsAtEnd = false

    override val listener = LightTNTListener(gm)

    override fun start() {
        super.start()

        val fs = ItemStack(Material.FLINT_AND_STEEL, 1)
        gm.players.keys.forEach {
            it.inventory.setItem(0, fs)
        }
        gm.spawnNBlocks(gm.players.keys.size, Material.TNT)
    }

    override fun end() {
        super.end()
        gm.despawnMob(EntityType.PRIMED_TNT)
    }

}
class LightTNTListener(private val gm: GameManager) : Listener {
    @EventHandler
    fun onPlayerUse(e: PlayerInteractEvent) {
        val p = e.player
        if (gm.isPlayer(p)) {
            p.sendMessage(e.action.toString())
            p.sendMessage((e.clickedBlock?.type).toString())
            p.sendMessage(p.activeItem.type.toString())
            p.sendMessage(e.item.toString())
            if (e.action == Action.RIGHT_CLICK_BLOCK && e.clickedBlock?.type == Material.TNT && e.item?.type == Material.FLINT_AND_STEEL ) {
                e.isCancelled = true
                gm.winRound(p)
            }
        }
    }

    @EventHandler
    fun onTNTExplode(e: EntityExplodeEvent) {
        if (e.entity.type == EntityType.PRIMED_TNT) {
                e.isCancelled = true
                // TODO: maybe do something cool in here with removing some of the platform
        }
    }
}