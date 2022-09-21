package pl.edu.pg.eti.domain.model


class PlayerGuessEvent(
    val nickname: String,
    val content: String
):RabbitEvent{
    override val routingKey = "S.X"//todo wrzucić do enuma
    override fun toCSV():String{
        return "GSS,${nickname},${content}"
    }
}