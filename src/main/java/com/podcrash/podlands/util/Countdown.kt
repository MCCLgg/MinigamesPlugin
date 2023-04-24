package com.podcrash.podlands.util

import com.podcrash.podlands.Main
import org.bukkit.scheduler.BukkitRunnable

class Countdown(private val time: Int, val onDown: (Int) -> Unit, val onEnd: () -> Unit): BukkitRunnable() {

    var timeRemaining = time

    override fun run() {
        onDown(timeRemaining)
        if (timeRemaining <= 0) {
            this.cancel()
            this.onEnd()
        } else {
            timeRemaining--
        }
    }

    fun start(plugin: Main) {
        this.runTaskTimer(plugin, 0, 20L)
    }


}