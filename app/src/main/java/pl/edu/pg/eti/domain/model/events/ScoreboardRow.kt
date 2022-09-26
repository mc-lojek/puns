package pl.edu.pg.eti.domain.model.events

class ScoreboardRow {
    constructor(nickname: String, score: Int){
        this.nickname=nickname
        this.score=score
    }
    val nickname: String
    val score: Int
    override fun toString(): String {
        return "$nickname,$score"
    }


}