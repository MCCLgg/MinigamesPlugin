package com.podcrash.podlands

import com.podcrash.podlands.commands.GameCommands
import com.podcrash.podlands.game.GameManager
import com.podcrash.podlands.game.Map
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.Audiences
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {


    override fun onEnable() {
        getLogger().info("Podlands INIT!")

        // TODO: Load maps from config

        val maps = Map.load(this)

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