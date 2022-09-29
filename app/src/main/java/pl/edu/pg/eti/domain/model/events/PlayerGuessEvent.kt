import pl.edu.pg.eti.domain.model.enums.RoutingKey
import pl.edu.pg.eti.domain.model.events.RabbitEvent

class PlayerGuessEvent : RabbitEvent {
    constructor(csv: String) {
        val arr = csv.split(",")
        this.id = arr[1].toLong()
        this.nickname = arr[2]
        this.content = arr[3]
    }

    constructor(
        id: Long,
        nickname: String,
        content: String,
    ) {
        this.id = id
        this.nickname = nickname
        this.content = content
    }


    override val routingKey = RoutingKey.TO_SERVER
    val id: Long
    val nickname: String
    val content: String


    override fun toCSV(): String {
        return "PGE,${id},${nickname},${content}"
    }
}