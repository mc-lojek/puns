package pl.edu.pg.eti.data.network

import pl.edu.pg.eti.data.dto.RoomJoinDto
import pl.edu.pg.eti.data.dto.UserPayloadDto
import retrofit2.http.*

interface ApiService {

    @POST("/api/game/room/join")
    suspend fun joinRoom(
        @Body userPayload: UserPayloadDto,
        @Header("Authorization") token: String
    ): RoomJoinDto


    @POST("/api/game/room/{roomId}/leave")
    suspend fun leaveRoom(
        @Path("roomId") roomId:Long,
        @Body userPayload: UserPayloadDto,
        @Header("Authorization") token: String
    ): RoomJoinDto


}