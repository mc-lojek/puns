package pl.edu.pg.eti.data.repository

import pl.edu.pg.eti.data.dto.UserPayloadDto
import pl.edu.pg.eti.data.mapper.toDomain
import pl.edu.pg.eti.data.network.ApiService
import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.domain.model.RoomJoin
import pl.edu.pg.eti.domain.repository.GameRepository
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(private val service: ApiService) : GameRepository {
    override suspend fun joinRoom(userId:Long,userName:String): Resource<RoomJoin> {
        return try {
            val response = service.joinRoom(
                UserPayloadDto(userName,userId),
                "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI4Iiwicm9sZXMiOlsiVVNFUiJdLCJleHAiOjE2NjU2ODY0NDV9.8Hu_hOkvxiIj9_uH33rP2oESh8Is6RwnaA7hb60wmSY"
            )
            Resource.Success(response.toDomain())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "UnknownError")
        }

    }

    override suspend fun leaveRoom(roomId: Long, userId:Long,userName:String): Resource<String> {
        return try {
            service.leaveRoom(
                roomId,
                UserPayloadDto(userName,userId),
                "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI4Iiwicm9sZXMiOlsiVVNFUiJdLCJleHAiOjE2NjU2ODY0NDV9.8Hu_hOkvxiIj9_uH33rP2oESh8Is6RwnaA7hb60wmSY"
            )
            Resource.Success("")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "UnknownError")
        }

    }


}