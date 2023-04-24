package com.podcrash.podlands.commands

import com.podcrash.podlands.game.GameManager
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GameCommands(private val gameManager: GameManager): CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            gameManager.playerManager.addPlayer(sender)
            gameManager.start()

            val c = Component.text("test")


            Audience.audience(sender).clearTitle()
        }

        return true;
    }
}
