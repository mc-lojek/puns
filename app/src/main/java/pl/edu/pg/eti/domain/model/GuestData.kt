package pl.edu.pg.eti.domain.model

import com.google.gson.annotations.SerializedName

class Guest(
    @SerializedName("username") val username: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("id") val id: String,
)