package rest_test


import edu.agh.susgame.dto.rest.model.LobbyDetails
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.LobbyRow
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals


class GameRestTest {

    @Test
    fun testGetAllGames() = testApplication {
        val client = createClient {
            install(HttpClient(CIO))
        }
        val response = client.get("/games")
        assertEquals(HttpStatusCode.OK, response.status)
        val responseBody = response.bodyAsText()
        val lobbyList: List<LobbyRow> = Json.decodeFromString(responseBody)
        assertEquals(3, lobbyList.size)
        assertEquals("Gra Michała", lobbyList[0].name)
    }


    @Test
    fun testGetGame() = testApplication {
        val client = createClient {
            install(HttpClient(CIO))
        }
        val response1 = client.get("/games/2137")
        assertEquals(HttpStatusCode.NotFound, response1.status)

        val response2 = client.get("/games/0")
        assertEquals(HttpStatusCode.OK, response2.status)
        val responseBody = response2.bodyAsText()
        val lobby = Json.decodeFromString<LobbyDetails>(responseBody)
        val expectedLobby = LobbyDetails(
            LobbyId(0),
            "Gra Michała",
            false,
            4,
            emptyList()
        )
        assertEquals(expectedLobby, lobby)
    }


    @Test
    fun testRemoveGame() = testApplication {
        val client = createClient {
            install(HttpClient(CIO))
        }
        var response = client.delete("/games/0")
        assertEquals(HttpStatusCode.OK, response.status)

        response = client.get("/games/0")
        assertEquals(HttpStatusCode.NotFound, response.status)

        response = client.delete("/games/0")
        assertEquals(HttpStatusCode.NotFound, response.status)

        response = client.delete("/games/2137")
        assertEquals(HttpStatusCode.NotFound, response.status)

        response = client.get("/games")
        assertEquals(HttpStatusCode.OK, response.status)
        val responseBody = response.bodyAsText()
        val lobbyList: List<LobbyRow> = Json.decodeFromString(responseBody)
        assertEquals(2, lobbyList.size)
        assertEquals(1, lobbyList[0].id.value)
    }

    @Test
    fun testGameMap() = testApplication {
        val client = createClient {
            install(HttpClient(CIO))
        }

        // Game not yet started
        val response1 = client.get("/games/map/0")
        assertEquals(HttpStatusCode.BadRequest, response1.status)

        // Game doesn't exist
        val response2 = client.get("/games/map/420")
        assertEquals(HttpStatusCode.NotFound, response2.status)

        // TODO GAME-108 Do the real testing by establishing a websocket connection on
        //  `WEBSOCKET ws://localhost/games/join`
        // val response3 = ...
        // assertEquals(HttpStatusCode.OK, response3.status)
    }

}
