package com.podcrash.podlands

import com.podcrash.podlands.commands.GameCommands
import com.podcrash.podlands.game.GameManager
import com.podcrash.podlands.game.Map
import com.podcrash.podlands.util.Position
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.*

class Main : JavaPlugin() {

    override fun onEnable() {
        getLogger().info("Podlands INIT!")

        // TODO: Load maps from config

        val config = this.config

        val maps: MutableList<Map> = mutableListOf()

        config.getConfigurationSection("Maps")?.getKeys(false)?.forEach {
            val mapName = config.getString("Maps.$it.Name")
            val worldName = config.getString("Maps.$it.WorldName")
            getLogger().info("Loaded $mapName in world $worldName")

            if (worldName == null || mapName == null) {
                getLogger().warning("Each map must specify a MapName and WorldName!")
                return;
            }

            val world = server.getWorld(worldName)
            if (world == null) {
                getLogger().severe("Failed to load world $worldName")
                return;
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

                getLogger().info("Loaded $spawn at $x, $y, $z")

                if (spawn.split("_")[0] == "Player") {
                    playerSpawns.add(Location(world, x, y, z))
                } else {
                    mobSpawns.add(Location(world, x, y, z))
                }
            }

            maps.add(Map(mapName, world, playerSpawns, mobSpawns, corner, corner2))
        }

        val gameManager = GameManager(this, maps)

        getCommand("gm")!!.setExecutor(GameCommands(gameManager))
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    fun registerListener(e: Listener) {
        server.pluginManager.registerEvents(e, this)
    }

    fun unRegisterListener(e: Listener) {
        HandlerList.unregisterAll(e)
    }


}