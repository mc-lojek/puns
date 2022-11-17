package pl.edu.pg.eti.domain.repository

import pl.edu.pg.eti.domain.model.GuestData
import pl.edu.pg.eti.domain.model.Tokens
import retrofit2.Response

interface LoginRepository {
    suspend fun loginUser(nick: String, pass: String): Response<Tokens>

    suspend fun loginGuest(): Response<GuestData>
}