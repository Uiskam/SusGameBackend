package edu.agh.susgame.back.net

import edu.agh.susgame.dto.socket.server.EdgeDTO
import edu.agh.susgame.config.*
import kotlin.math.ceil

/**
 * Represents an edge in a graph.
 *
 * @param index The index of the edge (Supposed to be unique).
 * @param weight The weight of the edge.
 */

class Edge(
    val index: Int,
    private var weight: Int,
    var transportedPacketsThisTurn: Int = 0,
    private var upgradeCost: Int = EDGE_BASE_UPGRADE_COST
) {

    fun getWeight() = weight

    fun toDTO() = EdgeDTO(index, upgradeCost, transportedPacketsThisTurn)

    fun getUpgradeCost() = upgradeCost

    /**
     * Upgrades the weight of the edge.
     * Increases the weight and upgrade cost of the edge by a predefined coefficients.
     */
    fun upgradeWeight(player: Player): Boolean {
        if (player.getCurrentMoney() < upgradeCost) {
            return false
        }
        player.setCurrentMoney(player.getCurrentMoney() - upgradeCost)
        weight += (ceil(EDGE_UPGRADE_WEIGHT_COEFF * weight)).toInt()
        upgradeCost += (ceil(EDGE_UPGRADE_COST_COEFF * upgradeCost)).toInt()
        return true
    }
}

