package pl.edu.pg.eti.data.repository

import pl.edu.pg.eti.data.dto.UserPayloadDto
import pl.edu.pg.eti.data.mapper.toDomain
import pl.edu.pg.eti.data.network.ApiService
import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.domain.manager.TokenManager
import pl.edu.pg.eti.domain.model.Room
import pl.edu.pg.eti.domain.model.RoomConfig
import pl.edu.pg.eti.domain.model.RoomJoin
import pl.edu.pg.eti.domain.repository.GameRepository
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val service: ApiService,
    private val tokenManager: TokenManager
    ) : GameRepository {
    override suspend fun joinRoom(
        userId: Long,
        userName: String,
        hash: String?
    ): Resource<RoomJoin> {
        return try {
            val response = if (hash == null) {
                service.joinRoom(
                    UserPayloadDto(userName, userId),
                    "JWT ${tokenManager.accessToken}"
                )
            } else {
                service.joinPrivateRoom(
                    hash, UserPayloadDto(userName, userId),
                    "JWT ${tokenManager.accessToken}"
                )
            }
            Resource.Success(response.toDomain())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "UnknownError")
        }

    }

    override suspend fun leaveRoom(roomId: Long, userId: Long, userName: String): Resource<String> {
        return try {
            service.leaveRoom(
                roomId,
                UserPayloadDto(userName, userId),
                "JWT ${tokenManager.accessToken}"
            )
            Resource.Success("")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "UnknownError")
        }

    }

    override suspend fun createPrivateRoom(roomConfig: RoomConfig): Resource<Room> {
        return try {
            val room = service.createPrivateRoom(
                roomConfig,
                "JWT ${tokenManager.accessToken}"
            )
            Resource.Success(room)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "UnknownError")
        }
    }
}