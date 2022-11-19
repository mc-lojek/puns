package pl.edu.pg.eti.domain.manager

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

class TokenManager {

    var accessToken: String? = null
    var refreshToken: String? = null
    var userId: Long? = null
    var username: String? = null
    var isGuest: Boolean? = null


    fun initialize(accessToken: String, refreshToken: String) {

        val trimmed = accessToken.substringAfter("JWT ")
        val alg = Algorithm.HMAC256("ACCESS_TOKEN_PUNS_SECRET".toByteArray())
        val verifier = JWT.require(alg).build()
        val decoded = verifier.verify(trimmed)

        userId = decoded.subject.toLong()
        username = decoded.claims["username"]?.asString()
        val roles = decoded.claims["roles"]?.asArray(String::class.java)
        isGuest = roles!!.contains("ROLE_GUEST")
        this.accessToken = accessToken.substringAfter("JWT ")
        this.refreshToken = refreshToken.substringAfter("JWT ")

    }

    fun logout() {
        accessToken = null
        refreshToken = null
        userId = null
        username = null
        isGuest = null
    }

}