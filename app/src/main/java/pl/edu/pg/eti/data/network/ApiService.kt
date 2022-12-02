package pl.edu.pg.eti.data.network

import pl.edu.pg.eti.data.dto.RoomJoinDto
import pl.edu.pg.eti.data.dto.UserPayloadDto
import pl.edu.pg.eti.domain.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("/api/game/room/join")
    suspend fun joinRoom(
        @Body userPayload: UserPayloadDto,
        @Header("Authorization") token: String
    ): RoomJoinDto


    @POST("/api/game/room/{roomId}/leave")
    suspend fun leaveRoom(
        @Path("roomId") roomId: Long,
        @Body userPayload: UserPayloadDto,
        @Header("Authorization") token: String
    ): RoomJoinDto


    @POST("/api/game/room/join/{hash}")
    suspend fun joinPrivateRoom(
        @Path("hash") roomHash: String,
        @Body userPayload: UserPayloadDto,
        @Header("Authorization") token: String
    ): RoomJoinDto

    @POST("/api/game/room/create")
    suspend fun createPrivateRoom(
        @Body roomConfig: RoomConfig,
        @Header("Authorization") token: String
    ): Room

    @GET("/api/users")
    suspend fun getAllUsers(): List<User>;

    @POST("/api/users/register")
    suspend fun registerUser(@Body user: User): Void

    @POST("/api/users/login")
    suspend fun loginUser(@Body userWithoutNick: UserWithoutNick): Tokens

    @POST("/api/users/guest")
    suspend fun loginGuest(): GuestData
}