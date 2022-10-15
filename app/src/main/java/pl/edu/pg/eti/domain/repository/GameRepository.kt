package pl.edu.pg.eti.domain.repository

import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.domain.model.RoomJoin

interface GameRepository {
    suspend fun joinRoom(UserId:Long): Resource<RoomJoin>
    suspend fun leaveRoom(roomId: Long, userId: Long): Resource<String>
}
