package com.podcrash.podlands.game

import com.podcrash.podlands.util.fillArea
import com.podcrash.podlands.util.fillAreaTile
import org.bukkit.*

class Map(
    val name: String,
    val world: World,
    val playerSpawns: List<Location>,
    val mobSpawns: List<Location>,
    private val corner: Location,
    private val corner2: Location
) {

    fun clearAbove(maxHeight: Int = 5) {
        fillArea(Location(world, corner.x, corner.y + 1, corner.z), Location(world, corner2.x, corner2.y + 1 + maxHeight, corner2.z), Material.AIR)
    }

    fun fillPlatform(mat: Material = Material.BLUE_CONCRETE) {
        fillArea(corner, corner2, mat)
    }

    fun tilePlatform(size: Int, materials: List<Material>) {
        fillAreaTile(corner, corner2, size, materials)
    }

}