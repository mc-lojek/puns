package pl.edu.pg.eti.domain.repository

import pl.edu.pg.eti.data.network.Resource


interface RegisterRepository {
    suspend fun registerUser(nick: String, pass: String, mail: String): Resource<Void>
}