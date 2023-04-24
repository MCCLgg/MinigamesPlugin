package com.podcrash.podlands.util

import org.bukkit.World
import org.bukkit.Location
import org.bukkit.Material
import kotlin.math.max
import kotlin.math.min

class Position(val x: Double, val y: Double, val z: Double) {

    fun toLocation(w: World): Location {
        return Location(w, x, y, z)
    }
}


fun fillArea(loc1: Location, loc2: Location, material: Material = Material.AIR) {
    val minX = min(loc1.blockX, loc2.blockX)
    val minY = min(loc1.blockY, loc2.blockY)
    val minZ = min(loc1.blockZ, loc2.blockZ)
    val maxX = max(loc1.blockX, loc2.blockX)
    val maxY = max(loc1.blockY, loc2.blockY)
    val maxZ = max(loc1.blockZ, loc2.blockZ)

    for (x in minX..maxX) {
        for (y in minY..maxY) {
            for (z in minZ..maxZ) {
                val blockLoc = Location(loc1.world, x.toDouble(), y.toDouble(), z.toDouble())
                blockLoc.block.type = material
            }
        }
    }
}

fun fillAreaTile(loc1: Location, loc2: Location, size: Int, materials: List<Material>) {
    val minX = min(loc1.blockX, loc2.blockX)
    val minY = min(loc1.blockY, loc2.blockY)
    val minZ = min(loc1.blockZ, loc2.blockZ)
    val maxX = max(loc1.blockX, loc2.blockX)
    val maxY = max(loc1.blockY, loc2.blockY)
    val maxZ = max(loc1.blockZ, loc2.blockZ)

    var mIdx = 0

    for (x in minX..maxX step 5) {
        for (y in minY..maxY) {
            for (z in minZ..maxZ step 5) {
                fillArea(
                    Location(loc1.world, x.toDouble(), y.toDouble(), z.toDouble()), Location(loc1.world,
                        (x + size - 1).toDouble(), y.toDouble(), (z + size - 1).toDouble()
                    ), materials[mIdx])

                if (mIdx < materials.size - 1) {
                    mIdx++
                } else {
                    mIdx = 0
                }

            }
        }
    }
}
