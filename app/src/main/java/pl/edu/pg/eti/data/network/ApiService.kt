package pl.edu.pg.eti.data.network

import pl.edu.pg.eti.data.dto.RoomJoinDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("/api/game/room/join")
    suspend fun joinRoom(
        @Query("userId") userId: Long,
        @Header("Authorization") token: String
    ): RoomJoinDto


}