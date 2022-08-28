package pl.edu.pg.eti.domain.model

import com.google.gson.annotations.SerializedName

data class UserWithoutNick(
    @SerializedName("username") val email: String,
    @SerializedName("password") val password: String,
) {
}