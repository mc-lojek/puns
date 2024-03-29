package pl.edu.pg.eti.data.mapper

import pl.edu.pg.eti.data.dto.RoomJoinDto
import pl.edu.pg.eti.domain.model.RoomJoin

fun RoomJoinDto.toDomain()=RoomJoin(
    exchangeName,
    queueName,
    hash,
    playersCount,
    maxPlayers,
    roundTime,
    players
)