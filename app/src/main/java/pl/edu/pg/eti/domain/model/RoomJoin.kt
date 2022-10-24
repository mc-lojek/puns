package pl.edu.pg.eti.domain.model

data class RoomJoin(
    val exchangeName: String,
    val queueName: String,
    val hash: String?
) {
    val isPrivate: Boolean
    get() = hash!=null
}