package pl.edu.pg.eti.data.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import pl.edu.pg.eti.data.service.ApiService
import pl.edu.pg.eti.domain.model.UserWithoutNick
import pl.edu.pg.eti.domain.repository.LoginRepository
import retrofit2.Response
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val apiService : ApiService) : LoginRepository {
    override suspend fun loginUser(nick:String, pass: String): Response<Void> {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", nick)
            .addFormDataPart("password", pass)
            .build();
        return apiService.loginUser(requestBody)
    }
}