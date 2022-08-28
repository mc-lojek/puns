package pl.edu.pg.eti.data.repository

import pl.edu.pg.eti.data.service.ApiService
import pl.edu.pg.eti.domain.model.User
import pl.edu.pg.eti.domain.repository.RegisterRepository
import retrofit2.Response
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val apiService : ApiService) : RegisterRepository{
    override suspend fun registerUser(nick: String, pass: String, mail: String): Response<User> {
        return apiService.registerUser(user = User(nick, mail, pass))
    }
}