package pl.edu.pg.eti.domain.model

import com.google.gson.annotations.SerializedName

data class UserWithoutNick(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
) {
}