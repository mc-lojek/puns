package pl.edu.pg.eti.domain.model

import com.google.gson.annotations.SerializedName
data class GuestData(
    @SerializedName("data") val guestData: Guest,
) {
}

class Guest(
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("id") val id: String,
)