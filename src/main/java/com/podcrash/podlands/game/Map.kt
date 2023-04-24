package com.podcrash.podlands.game

import com.podcrash.podlands.Main
import com.podcrash.podlands.util.Random.Companion.getRandomObjects
import com.podcrash.podlands.util.fillArea
import com.podcrash.podlands.util.fillAreaTile
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.CreatureSpawnEvent

class Map(
    val name: String,
    val world: World,
    val playerSpawns: List<Location>,
    val mobSpawns: List<Location>,
    private val corner: Location,
    private val corner2: Location
) {

    private val gameRules = mapOf(
        GameRule.DO_MOB_LOOT to false,
        GameRule.DO_MOB_SPAWNING to false,
        GameRule.DO_DAYLIGHT_CYCLE to false,
        GameRule.KEEP_INVENTORY to false,
        GameRule.NATURAL_REGENERATION to false,
    )

    init {
        gameRules.forEach { (k, v) -> world.setGameRule(k, v) }
    }

    companion object {
        fun load(plugin: Main): List<Map> {
            val maps: MutableList<Map> = mutableListOf()
            val config = plugin.config
            val logger = plugin.getLogger()

            config.getConfigurationSection("Maps")?.getKeys(false)?.forEach {
                val mapName = config.getString("Maps.$it.Name")
                val worldName = config.getString("Maps.$it.WorldName")
                logger.info("Loaded $mapName in world $worldName")
                if (worldName == null || mapName == null) {
                    logger.warning("Each map must specify a MapName and WorldName!")
                    return@forEach
                }
                val world = plugin.server.getWorld(worldName)
                if (world == null) {
                    logger.severe("Failed to load world $worldName")
                    return@forEach
                }
                val playerSpawns: MutableList<Location> = mutableListOf()
                val mobSpawns: MutableList<Location> = mutableListOf()

                val x1 = config.getDouble("Maps.$it.X1")
                val y1 = config.getDouble("Maps.$it.Y1")
                val z1 = config.getDouble("Maps.$it.Z1")

                val x2 = config.getDouble("Maps.$it.X2")
                val y2 = config.getDouble("Maps.$it.Y2")
                val z2 = config.getDouble("Maps.$it.Z2")

                val corner = Location(world, x1, y1, z1)
                val corner2 = Location(world, x2, y2, z2)

                config.getConfigurationSection("Maps.$it.Spawns")?.getKeys(false)?.forEach { spawn ->
                    val x = config.getDouble("Maps.$it.Spawns.$spawn.X")
                    val y = config.getDouble("Maps.$it.Spawns.$spawn.Y")
                    val z = config.getDouble("Maps.$it.Spawns.$spawn.Z")

                    if (spawn.split("_")[0] == "Player") {
                        playerSpawns.add(Location(world, x, y, z))
                        logger.info("Loaded player spawn at $x, $y, $z")
                    } else {
                        mobSpawns.add(Location(world, x, y, z))
                        logger.info("Loaded mob spawn at $x, $y, $z")
                    }
                }
                maps.add(Map(mapName, world, playerSpawns, mobSpawns, corner, corner2))
            }

            return maps
        }
    }

    fun clearAbove(maxHeight: Int = 5) {
        fillArea(
            Location(world, corner.x, corner.y + 1, corner.z),
            Location(world, corner2.x, corner2.y + 1 + maxHeight, corner2.z),
            Material.AIR
        )
    }

    fun fillPlatform(mat: Material = Material.BLUE_CONCRETE) {
        fillArea(corner, corner2, mat)
    }

    fun tilePlatform(size: Int, materials: List<Material>) {
        fillAreaTile(corner, corner2, size, materials)
    }

    fun offPlatformHeight(): Int {
        // TODO: Change with map
        return 50
    }

    fun getWinningLocation(): Location {
        // TODO: Change with map
        return Location(world, -15.0, 80.0, 15.0)
    }

    private fun spawnMob(
        loc: Location,
        type: EntityType,
        spawnReason: CreatureSpawnEvent.SpawnReason = CreatureSpawnEvent.SpawnReason.NATURAL
    ) {
        world.spawnEntity(loc, type, spawnReason)
    }

    fun spawnBlock(loc: Location, block: Material) {
        world.setType(loc, block)
    }

    fun despawnMob(type: EntityType) {
        world.entities.forEach {
            if (it.type == type) {
                it.remove()
            }
        }
    }

    fun spawnNMobs(
        num: Int,
        type: EntityType,
        spawnReason: CreatureSpawnEvent.SpawnReason = CreatureSpawnEvent.SpawnReason.NATURAL
    ) {
        val locs = getRandomObjects(num, mobSpawns)
        locs.forEach {
            spawnMob(it, type, spawnReason)
        }
    }

    fun spawnNBlocks(num: Int, block: Material) {
        val locs = getRandomObjects(num, mobSpawns)
        locs.forEach {
            spawnBlock(it, block)
        }
    }

}