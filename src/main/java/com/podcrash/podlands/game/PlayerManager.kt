package com.podcrash.podlands.game

import com.podcrash.podlands.Main
import com.podcrash.podlands.util.Random
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.Audiences
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.sound.Sound.sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import java.time.Duration

class PlayerManager(
    val minPlayers: Int = 8,
    val maxPlayers: Int = 16
) {

    private var players = mutableMapOf<Player, Int>()

    fun count(): Int {
        return getPlayers().size;
    }

    fun getPlayers(): List<Player> {
        return players.keys.toList()
    }

    fun isPlayer(p: Player): Boolean {
        return players.contains(p)
    }

    fun getScoreByPlayer(p: Player): Int {
        return getPlayers().indexOf(p)
    }

    fun announce(text: String, color: NamedTextColor = NamedTextColor.WHITE) {
        toAudience().sendMessage(Component.text(text, color))
    }

    fun sound(sound: Sound, pitch: Float = 1f, volume: Float = 1f) {
        toAudience().playSound(sound(sound, net.kyori.adventure.sound.Sound.Source.MASTER, volume, pitch))
    }

    fun actionbar(text: String, color: NamedTextColor = NamedTextColor.WHITE) {
        toAudience().sendActionBar(Component.text(text, color))
    }

    fun clearInventory() {
        getPlayers().forEach {
            it.inventory.clear()
        }
    }

    fun setInventory(i: ItemStack, x: Int = 0) {
        getPlayers().forEach {
            it.inventory.setItem(x, i)
        }

    }

    fun setFoodLevel(x: Int = 0) {
        getPlayers().forEach {
            it.foodLevel = x
        }
    }

    fun gamemode(gm: GameMode = GameMode.ADVENTURE) {
        getPlayers().forEach {
            it.gameMode = gm
        }
    }

    fun addPlayer(player: Player) {
        players[player] = 0
    }

    fun removePlayer(player: Player) {
        players.remove(player)
    }

    private fun addPoint(player: Player, amount: Int = 1) {
        players[player] = amount + players[player]!!
    }

    fun winnersPoint(failList: List<Player>) {
        getPlayers().forEach {
            if (!failList.contains(it)) {
                addPoint(it)
            }
        }
    }

    fun winRound(p: Player) {
        addPoint(p)
        p.playSound(p.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.0f)
        p.inventory.clear()
    }

    fun winRoundAndTeleport(p: Player, map: Map) {
        addPoint(p)
        p.playSound(p.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.0f)
        p.inventory.clear()
        p.allowFlight = true
        p.teleport(map.getWinningLocation())
    }

    fun spreadPlayers(map: Map) {
        getPlayers().forEach {
            it.teleport(Random.getRandomObject(map.playerSpawns))
        }
    }

    private fun toAudience(): ForwardingAudience {
        return Audience.audience(players.keys)
    }

    fun title(
        title: String = "",
        titleColor: NamedTextColor = NamedTextColor.WHITE,
        subtitle: String = "",
        subtitleColor: NamedTextColor = NamedTextColor.WHITE,
        fadeIn: Long = 500,
        stay: Long = 2000,
        fadeOut: Long = 500
    ) {
        toAudience().showTitle(
            Title.title(
                Component.text(title, titleColor),
                Component.text(subtitle, subtitleColor),
                Title.Times.times(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut))
            )
        )
    }

    fun clearTitle() {
        toAudience().clearTitle()
    }

    fun createScoreboard(plugin: Main): Int {
        return plugin.server.scheduler.scheduleSyncRepeatingTask(plugin, {
            val manager = Bukkit.getScoreboardManager()
            val board = manager.newScoreboard
            val objective = board.registerNewObjective("test", Criteria.DUMMY.name, "Scores")
            objective.displaySlot = DisplaySlot.SIDEBAR
            objective.displayName = ChatColor.RED.toString() + "Scores"

            players.keys.forEach {
                val score = objective.getScore(it.name)
                score.score = players[it]!!
            }
            players.keys.forEach { it.scoreboard = board }
        }, 0, 5L)
    }

}