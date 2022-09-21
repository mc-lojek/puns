package pl.edu.pg.eti.data.repository

import pl.edu.pg.eti.data.mapper.toDomain
import pl.edu.pg.eti.data.network.ApiService
import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.domain.model.RoomJoin
import pl.edu.pg.eti.domain.repository.GameRepository
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(private val service: ApiService):GameRepository {
    override suspend fun joinRoom(userId: Long): Resource<RoomJoin> {
        return try{
            val response = service.joinRoom(userId,"JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI4Iiwicm9sZXMiOlsiVVNFUiJdLCJleHAiOjE2NjQ1NTg1MzR9.v_4dwYFGhoJ1e2jC-5VTaT5NhT2IQlfh5nzP7IQ4pTs")
            Resource.Success(response.toDomain())
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.message?:"UnknownError")
        }

    }
}