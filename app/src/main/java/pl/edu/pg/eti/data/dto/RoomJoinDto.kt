package pl.edu.pg.eti.data.dto

data class RoomJoinDto(
    val queueName: String,
    val room: RoomDto,
    val id: Long,
    val players: List<String>
) {

}

data class RoomDto(
    val name: String,
    val state: String,
    val playersCount: Int,
    val maxPlayers: Int,
    val id: Long,
    val hash: String?,
    val roundTime:Long
)