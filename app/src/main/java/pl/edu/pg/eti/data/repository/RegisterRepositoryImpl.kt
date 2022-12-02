package pl.edu.pg.eti.data.repository

import pl.edu.pg.eti.data.network.ApiService
import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.domain.model.User
import pl.edu.pg.eti.domain.model.UserWithoutNick
import pl.edu.pg.eti.domain.repository.RegisterRepository
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val apiService : ApiService) : RegisterRepository{
    override suspend fun registerUser(nick: String, pass: String, mail: String): Resource<Void> {
        return try {
            val response = apiService.registerUser(user = User(nick, mail, pass))
            Resource.Success(response)
        }catch (ex: HttpException){
            Resource.Error(ex.response()?.errorBody()?.string() ?: "UnknownError")
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.message ?: "UnknownError")
        }
    }
}