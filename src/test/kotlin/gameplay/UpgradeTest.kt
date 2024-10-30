package gameplay

import edu.agh.susgame.back.net.Player
import edu.agh.susgame.back.net.parser.GraphParser
import edu.agh.susgame.config.*
import org.junit.Test
import kotlin.math.ceil
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UpgradeTest {
    private val parser = GraphParser()
    @Test
    fun upgradeTest() {
        val player0 = Player(0, "P0")
        val player1 = Player(1, "P1")
        val filePath = "src/main/resources/graph_files/test/test_upgrade.json"
        val players = listOf(player0, player1)
        val graph = parser.parseFromFile(filePath, players)


        val edge = graph.getEdges().first()
        player0.setCurrentMoney(EDGE_BASE_UPGRADE_COST)
        val expectedWeightAfterUpgrade = edge.getWeight() + ceil(edge.getWeight() * EDGE_UPGRADE_WEIGHT_COEFF).toInt()
        var result = edge.upgradeWeight(player0)
        // check if upgrade success when player has enough money
        assertTrue { result }
        result = edge.upgradeWeight(player0)
        // check if upgrade fail when player has not enough money
        assertTrue { !result }
        // check the decrease of the money after upgrade
        assertEquals(0, player0.getCurrentMoney())
        // check the increase of the weight after upgrade
        assertEquals(expectedWeightAfterUpgrade ,edge.getWeight())
        // check the increase of the upgrade cost
        assertEquals(EDGE_BASE_UPGRADE_COST + ceil(EDGE_BASE_UPGRADE_COST * EDGE_UPGRADE_COST_COEFF).toInt(),
            edge.getUpgradeCost())

        val router = graph.getRoutersList().first()
        player0.setCurrentMoney(ROUTER_BASE_UPGRADE_COST)
        val expectedWeightAfterUpgradeRouter = router.getBufferSize() + ceil(router.getBufferSize() * ROUTER_UPGRADE_CAPACITY_COEFF).toInt()
        result = router.upgradeBuffer(player0)
        // check if upgrade success when player has enough money
        assertTrue { result }
        result = router.upgradeBuffer(player0)
        // check if upgrade fail when player has not enough money
        assertTrue { !result }
        // check the decrease of the money after upgrade
        assertEquals(0, player0.getCurrentMoney())
        // check the increase of capacity after upgrade
        assertEquals(expectedWeightAfterUpgradeRouter ,router.getBufferSize())
        // check the increase of the upgrade cost
        assertEquals(ROUTER_BASE_UPGRADE_COST + ceil(ROUTER_BASE_UPGRADE_COST * ROUTER_UPGRADE_COST_COEFF).toInt(),
            router.getUpgradeCost())


    }

}