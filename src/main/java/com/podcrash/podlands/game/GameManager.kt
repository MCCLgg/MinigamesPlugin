package com.podcrash.podlands.game

import com.podcrash.podlands.Main
import com.podcrash.podlands.game.mini.*
import com.podcrash.podlands.util.Countdown
import com.podcrash.podlands.util.Random.Companion.CONCRETES
import com.podcrash.podlands.util.Random.Companion.getRandomObjects
import net.kyori.adventure.audience.Audience
import org.bukkit.*
import org.bukkit.scheduler.BukkitRunnable

class GameManager(
    private val plugin: Main,
    private val maps: List<Map>,
    private val rounds: Int = 5
) {

    val playerManager: PlayerManager = PlayerManager()

    var gameState: GameState = GameState.Lobby

    var currentGameIdx: Int = -1

    private var currentMapIdx: Int = 0

    private var scoreboardTaskId: Int = -1

    var round: Int = 1

    private val games: List<Minigame> = listOf( DontMoveGame(this), ShearGame(this), VoidJumpGame(this), LightTNTGame(this), EatCakeGame(this), BreakTorchGame(this))

    fun getMap(): Map {
        return maps[currentMapIdx]
    }

    fun changeState(s: GameState) {
        s.onLeave()
        gameState = s
        s.onEnter()
    }

    private fun chooseGame() {
        currentGameIdx = (games.indices).random()
    }

    fun getCurrentGame(): Minigame {
        return games[currentGameIdx]
    }

    fun start() {
        scoreboardTaskId = playerManager.createScoreboard(plugin)

        getMap().tilePlatform(5, getRandomObjects(2, CONCRETES))

        changeState(GameState.Game)
        round = 1
        newGame()
    }

    private fun end() {
        plugin.server.scheduler.cancelTask(scoreboardTaskId)
        changeState(GameState.Lobby)
    }

    // Choose a new micro game to start and set it up
    private fun newGame() {
        chooseGame()
        getCurrentGame().start()

        getMap().tilePlatform(5, getRandomObjects(2, CONCRETES))

        /*
        if (round == 5) {
            getMap().fillPlatform(Material.AIR)
            getMap().tilePlatform(4, listOf(Material.YELLOW_CONCRETE, Material.LIGHT_BLUE_CONCRETE))
        }

        if (round == 7) {
            getMap().fillPlatform(Material.AIR)
            getMap().tilePlatform(3, listOf(Material.YELLOW_CONCRETE, Material.LIGHT_BLUE_CONCRETE))
        }
         */

        plugin.registerListener(getCurrentGame().listener)

        val c = Countdown(getCurrentGame().duration, { timeRemaining -> playerManager.actionbar("Time: $timeRemaining") }, { endGame() })
        c.start(plugin)
    }

    // End round/micro game
    fun endGame() {
        plugin.unRegisterListener(getCurrentGame().listener)
        getCurrentGame().end()
        getMap().clearAbove()

        if (round < rounds) {
            round++
            plugin.server.scheduler.scheduleSyncDelayedTask(plugin, { newGame() } , 20L * 3)
        } else {
            end()
        }
    }
}