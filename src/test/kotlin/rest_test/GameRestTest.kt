package rest_test


import edu.agh.susgame.back.main
import edu.agh.susgame.back.module
import edu.agh.susgame.dto.rest.games.model.GameCreationRequest
import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.LobbyId
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.*
import kotlinx.serialization.json.Json


class GameRestTest {

    @Test
    fun testGetAllGames() = testApplication {
        val client = createClient {
            install(HttpClient(CIO))
        }
        val response = client.get("/games")
        assertEquals(HttpStatusCode.OK, response.status)
        val responseBody = response.bodyAsText()
        val lobbyList: List<Lobby> = Json.decodeFromString(responseBody)
        assertEquals(4, lobbyList.size)
        assertEquals("Gra do testowania v0.1 engine", lobbyList[0].name)
    }


    @Test
    fun testGetGame() = testApplication {
        val client = createClient {
            install(HttpClient(CIO))
        }
        var response: HttpResponse

        response = client.get("/games/2137")
        assertEquals(HttpStatusCode.NotFound, response.status)

        response = client.get("/games/0")
        assertEquals(HttpStatusCode.OK, response.status)
        val responseBody = response.bodyAsText()
        val lobby = Json.decodeFromString<Lobby>(responseBody)
        val expectedLobby = Lobby(
            LobbyId(0),
            "Gra do testowania v0.1 engine",
            4,
            10,
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
        val lobbyList: List<Lobby> = Json.decodeFromString(responseBody)
        assertEquals(3, lobbyList.size)
        assertEquals(1, lobbyList[0].id.value)
    }

}