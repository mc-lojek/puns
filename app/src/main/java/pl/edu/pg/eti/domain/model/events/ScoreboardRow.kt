package pl.edu.pg.eti.domain.model.events

class ScoreboardRow {
    constructor(nickname: String, totalScore: Int, roundScore: Int){
        this.nickname=nickname
        this.totalScore=totalScore
        this.roundScore=roundScore
    }
    val nickname: String
    val totalScore: Int
    val roundScore: Int

    override fun toString(): String {
        return "$nickname,$totalScore,$roundScore"
    }


}