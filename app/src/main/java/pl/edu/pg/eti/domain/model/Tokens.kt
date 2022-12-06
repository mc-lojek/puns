package pl.edu.pg.eti.domain.model

import com.google.gson.annotations.SerializedName

data class Tokens(
    @SerializedName("accessToken") val access_token: String,
    @SerializedName("refreshToken") val refresh_token: String,
) {
}