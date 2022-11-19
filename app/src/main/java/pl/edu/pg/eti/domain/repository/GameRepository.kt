package pl.edu.pg.eti.domain.repository

import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.domain.model.Room
import pl.edu.pg.eti.domain.model.RoomConfig
import pl.edu.pg.eti.domain.model.RoomJoin

interface GameRepository {
    suspend fun joinRoom(userId: Long, userName: String, hash: String? = null): Resource<RoomJoin>
    suspend fun leaveRoom(roomId: Long, userId: Long, userName: String): Resource<String>
    suspend fun createPrivateRoom(roomConfig: RoomConfig): Resource<Room>
}
