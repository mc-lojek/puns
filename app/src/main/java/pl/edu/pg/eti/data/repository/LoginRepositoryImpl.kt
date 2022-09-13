package pl.edu.pg.eti.data.repository

import pl.edu.pg.eti.data.service.ApiService
import pl.edu.pg.eti.domain.model.Tokens
import pl.edu.pg.eti.domain.model.UserWithoutNick
import pl.edu.pg.eti.domain.repository.LoginRepository
import retrofit2.Response
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val apiService : ApiService) : LoginRepository {
    override suspend fun loginUser(nick:String, pass: String): Response<Tokens> {
        return apiService.loginUser(userWithoutNick = UserWithoutNick(nick, pass))
    }
}