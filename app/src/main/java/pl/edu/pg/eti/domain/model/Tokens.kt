package pl.edu.pg.eti.domain.model

import com.google.gson.annotations.SerializedName

data class Tokens(
    @SerializedName("access_token") val access_token: String,
    @SerializedName("refresh_token") val refresh_token: String,
) {
}