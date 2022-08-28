package pl.edu.pg.eti.domain.repository

import retrofit2.Response

interface LoginRepository {
    suspend fun loginUser(nick: String, pass: String): Response<Void>
}