package pl.edu.pg.eti.data.service

import pl.edu.pg.eti.domain.model.Tokens
import pl.edu.pg.eti.domain.model.User
import pl.edu.pg.eti.domain.model.UserWithoutNick
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/api/users")
    suspend fun getAllUsers(): List<User>;

    @POST("/api/users/register")
    suspend fun registerUser(
        @Body user: User
        ): Response<Void>

    @POST("/api/users/login")
    suspend fun  loginUser(
        @Body userWithoutNick: UserWithoutNick
        ):Response<Tokens>
}