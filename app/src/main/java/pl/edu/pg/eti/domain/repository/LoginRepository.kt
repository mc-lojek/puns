package pl.edu.pg.eti.domain.repository

import pl.edu.pg.eti.data.network.Resource
import pl.edu.pg.eti.domain.model.Guest
import pl.edu.pg.eti.domain.model.Tokens

interface LoginRepository {
    suspend fun loginUser(nick: String, pass: String): Resource<Tokens>

    suspend fun loginGuest(): Resource<Guest>
}