package pl.edu.pg.eti.data.repository

import pl.edu.pg.eti.data.network.ApiService
import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.domain.model.Guest
import pl.edu.pg.eti.domain.model.Tokens
import pl.edu.pg.eti.domain.model.UserWithoutNick
import pl.edu.pg.eti.domain.repository.LoginRepository
import retrofit2.HttpException
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val apiService : ApiService) : LoginRepository {
    override suspend fun loginUser(nick:String, pass: String): Resource<Tokens> {
        return try {
            val response = apiService.loginUser(userWithoutNick = UserWithoutNick(nick, pass))
            Resource.Success(response)
        }catch (ex: HttpException){
            Resource.Error(ex.response()?.errorBody()?.string() ?: "UnknownError")
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.message ?: "UnknownError")
        }
    }

    override suspend fun loginGuest(): Resource<Guest>{
        return try {
            val response = apiService.loginGuest()
            Resource.Success(response)
        }catch(ex: HttpException){
            Resource.Error(ex.response()?.errorBody()?.string() ?: "UnknownError")
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.message ?: "UnknownError")
        }
    }
}