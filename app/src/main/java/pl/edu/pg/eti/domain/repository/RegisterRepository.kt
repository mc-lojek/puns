package pl.edu.pg.eti.domain.repository

import pl.edu.pg.eti.domain.model.User
import retrofit2.Response


interface RegisterRepository {
    suspend fun registerUser(nick: String, pass: String, mail: String): Response<User>
}