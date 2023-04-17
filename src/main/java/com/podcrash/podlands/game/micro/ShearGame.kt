package com.podcrash.podlands.game.micro

import com.podcrash.podlands.game.GameManager
import com.podcrash.podlands.game.Minigame
import org.bukkit.Material
import org.bukkit.Sound
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

    override val addPointsAtEnd = false

    override val listener = ShearListener(gm)

    override fun start() {
        super.start()

        val shears = ItemStack(Material.SHEARS, 1)
        gm.players.keys.forEach {
            it.inventory.setItem(0, shears)
        }
        gm.spawnNMobs(gm.players.keys.size, EntityType.SHEEP, CreatureSpawnEvent.SpawnReason.SHEARED)
    }

    override fun end() {
        super.end()
        gm.despawnMob(EntityType.SHEEP)
    }

}
class ShearListener(private val gm: GameManager) : Listener {
    @EventHandler
    fun onPlayerSheared(event: PlayerShearEntityEvent) {
        val p = event.player

        if (gm.isPlayer(p)) {
            event.isCancelled = true
            event.entity.remove()
            gm.winRound(p)
        }

    }
}