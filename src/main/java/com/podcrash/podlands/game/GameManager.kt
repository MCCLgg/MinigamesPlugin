package com.podcrash.podlands.game

import com.podcrash.podlands.Main
import com.podcrash.podlands.game.micro.*
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot
import kotlin.random.Random

class GameManager(
    private val plugin: Main,
    private val maps: List<Map>,
    val minPlayers: Int = 8,
    val maxPlayers: Int = 16,
    private val rounds: Int = 10
) {

    // TODO: make this private and make functions for each necessary player interaction
    var players = mutableMapOf<Player, Int>()

    var gameState: GameState = GameState.WAITING
    var currentGameIdx: Int = -1

    private var scoreboardTaskId: Int = -1

    var round: Int = 1

    private val games: List<Minigame> = listOf( DontMoveGame(this), ShearGame(this), VoidJumpGame(this), LightTNTGame(this), EatCakeGame(this), BreakTorchGame(this))

    private var currentMapIdx: Int = 0

    fun setMap(x: Int) {
        currentMapIdx = x
    }

    private fun getMap(): Map {
        return maps[currentMapIdx]
    }

    fun offPlatformHeight(): Int {
        // TODO: Change with map
        return 50;
    }

    private fun getWinningLocation(): Location {
        // TODO: Change with map
        return Location(world(), -15.0, 80.0, 15.0);
    }

    fun addPlayer(player: Player) {
        players[player] = 0
    }

    fun removePlayer(player: Player) {
        players.remove(player)
    }

    fun isPlayer(player: Player): Boolean {
        return players.contains(player)
    }

    private fun chooseGame() {
        currentGameIdx = (games.indices).random()
    }

    fun getCurrentGame(): Minigame {
        return games[currentGameIdx]
    }

    private fun addPoint(player: Player, amount: Int = 1) {
        players[player] = amount + players[player]!!
    }

    fun winRound(p: Player, teleport: Boolean = false) {
        addPoint(p)
        p.playSound(p.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.0f)
        p.inventory.clear()

        if (teleport) {
            p.allowFlight = true
            p.teleport(getWinningLocation())
        }
    }

    fun winnersPoint(failList: List<Player>) {
        players.keys.forEach {
            if (!failList.contains(it)) {
                addPoint(it)
            }
        }
    }

    fun getState(): GameState {
        return gameState;
    }

    private fun createScoreboard() {
        scoreboardTaskId = plugin.server.scheduler.scheduleSyncRepeatingTask(plugin, {
            val manager = Bukkit.getScoreboardManager()
            val board = manager.newScoreboard
            val objective = board.registerNewObjective("test", "dummy")
            objective.displaySlot = DisplaySlot.SIDEBAR
            objective.displayName = ChatColor.RED.toString() + "Scores"
            players.keys.forEach {
                val score = objective.getScore(it.name)
                score.score = players[it]!!
            }
            players.keys.forEach { it.scoreboard = board }
        },0, 5L)
    }

    private fun <T> getRandomObjects(numObjects: Int, objectList: List<T>): List<T> {
        if (numObjects > objectList.size) {
            throw IllegalArgumentException("The number of objects requested cannot be greater than the size of the list.")
        }

        val selectedObjects = mutableListOf<T>()
        val randomIndices = mutableListOf<Int>()

        while (selectedObjects.size < numObjects) {
            val randomIndex = Random.nextInt(objectList.size)
            if (randomIndex !in randomIndices) {
                randomIndices.add(randomIndex)
                selectedObjects.add(objectList[randomIndex])
            }
        }

        return selectedObjects
    }

    fun spawnMob(loc: Location, type: EntityType, spawnReason: CreatureSpawnEvent.SpawnReason = CreatureSpawnEvent.SpawnReason.NATURAL) {
        world().spawnEntity(loc, type, spawnReason)
    }

    fun spawnNMobs(num: Int, type: EntityType, spawnReason: CreatureSpawnEvent.SpawnReason = CreatureSpawnEvent.SpawnReason.NATURAL) {
        val locs = getRandomObjects(num, getMap().mobSpawns)
        locs.forEach {
            spawnMob(it.toLocation(world()), type, spawnReason)
        }
    }

    fun spawnBlock(loc: Location, block: Material) {
        world().setType(loc, block)
    }

    fun spawnNBlocks(num: Int, block: Material) {
        val locs = getRandomObjects(num, getMap().mobSpawns)
        locs.forEach {
            spawnBlock(it.toLocation(world()), block)
        }
    }


    fun despawnMob(type: EntityType) {
        world().entities.forEach {
            if (it.type == type) {
                it.remove()
            }
        }
    }

    private fun world(): World {
       return getMap().world
    }

    fun start() {
        createScoreboard()
        getMap().tilePlatform(5, listOf(Material.YELLOW_CONCRETE, Material.LIGHT_BLUE_CONCRETE))

        gameState = GameState.ACTIVE
        round = 1
        newGame()
    }

    private fun end() {
        plugin.server.scheduler.cancelTask(scoreboardTaskId)
    }

    private fun newGame() {
        chooseGame()
        getCurrentGame().start()

        if (round == 5) {
            getMap().fillPlatform(Material.AIR)
            getMap().tilePlatform(4, listOf(Material.YELLOW_CONCRETE, Material.LIGHT_BLUE_CONCRETE))
        }

        if (round == 7) {
            getMap().fillPlatform(Material.AIR)
            getMap().tilePlatform(3, listOf(Material.YELLOW_CONCRETE, Material.LIGHT_BLUE_CONCRETE))
        }

        plugin.registerListener(getCurrentGame().listener)

        object: BukkitRunnable() {
            var timeRemaining = getCurrentGame().duration
            override fun run() {
                actionbar("Time: $timeRemaining")
                if (timeRemaining <= 0) {
                    this.cancel()
                    endGame()
                } else {
                    timeRemaining--;
                }
            }
        }.runTaskTimer(plugin, 0, 20L)
    }

    fun endGame() {
        plugin.unRegisterListener(getCurrentGame().listener)
        getCurrentGame().end()
        getMap().clearAbove()

        round++
        if (round < rounds) {
            plugin.server.scheduler.scheduleSyncDelayedTask(plugin, { newGame() } , 20L * 3)
        } else {
            end()
        }
    }

    fun announceMsg(message: String) {
        players.keys.forEach {
            it.sendMessage(message)
        }
    }

    fun announceSound(sound: Sound, pitch: Float = 1f, volume: Float = 1f) {
        players.keys.forEach {
            it.playSound(it.location, sound, volume, pitch)
        }
    }

    fun actionbar(text: String) {
        players.keys.forEach {
            it.sendActionBar(Component.text(text))
        }
    }

    fun clearInventory() {
        players.keys.forEach {
            it.inventory.clear()
        }
    }
}