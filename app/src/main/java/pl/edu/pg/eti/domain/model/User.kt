package pl.edu.pg.eti.domain.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("username") val userName: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
) {
}