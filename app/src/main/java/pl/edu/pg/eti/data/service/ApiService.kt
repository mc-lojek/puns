package pl.edu.pg.eti.data.service

import okhttp3.RequestBody
import pl.edu.pg.eti.domain.model.User
import pl.edu.pg.eti.domain.model.UserWithoutNick
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/api/users")
    suspend fun getAllUsers(): List<User>;

    @POST("/api/register")
    suspend fun registerUser(
        @Body user: User
        ): Response<User>

    @POST("/api/login")
    suspend fun  loginUser(
        @Body requestBody: RequestBody
        ):Response<Void>
}