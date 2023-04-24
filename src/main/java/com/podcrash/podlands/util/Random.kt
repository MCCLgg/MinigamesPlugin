package com.podcrash.podlands.util

import org.bukkit.Material
import kotlin.random.Random

class Random {
    companion object {
        val CONCRETES = listOf(Material.BLUE_CONCRETE, Material.LIGHT_BLUE_CONCRETE, Material.YELLOW_CONCRETE, Material.CYAN_CONCRETE, Material.BLACK_CONCRETE, Material.BROWN_CONCRETE, Material.GREEN_CONCRETE, Material.PINK_CONCRETE, Material.ORANGE_CONCRETE, Material.PURPLE_CONCRETE, Material.RED_CONCRETE, Material.MAGENTA_CONCRETE)

        fun <T> getRandomObject(list: List<T>): T {
            val randomIndex = (list.indices).random()
            return list[randomIndex]
        }

        fun <T> getRandomObjects(numObjects: Int, objectList: List<T>): List<T> {
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
    }
}