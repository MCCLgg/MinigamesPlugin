package com.podcrash.podlands.game.micro

import com.podcrash.podlands.game.GameManager
import com.podcrash.podlands.game.Minigame
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack

class VoidJumpGame(gm: GameManager) : Minigame(gm) {

    override val name = "Jump!"
    override val duration = 5
    override val instructions = "Jump Off The Platform"

    override val addPointsAtEnd = false

    override val listener = VoidJumpListener(gm)

}
class VoidJumpListener(private val gm: GameManager) : Listener {

    private val winHeight = gm.offPlatformHeight()

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val p = event.player

        if (gm.isPlayer(p) && p.location.y < winHeight ) {
            gm.winRound(p, true)
        }

    }
}