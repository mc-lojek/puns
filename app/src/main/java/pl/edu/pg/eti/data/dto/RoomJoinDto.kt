package pl.edu.pg.eti.data.dto

data class RoomJoinDto(
    val queueName: String,
    val exchangeName: String,
    val roomId: Long,
    val type: String,
    val hash: String?,
    val turnsCount: Int,
    val maxPlayers: Int,
    val roundTime: Long,
    val playersCount: Int,
    val players: List<String>,
)