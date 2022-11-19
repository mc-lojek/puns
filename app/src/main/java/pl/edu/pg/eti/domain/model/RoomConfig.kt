package pl.edu.pg.eti.domain.model

data class RoomConfig (
    val turnsCount: Int,
    val maxPlayers: Int,
    val roundTime: Long,
)