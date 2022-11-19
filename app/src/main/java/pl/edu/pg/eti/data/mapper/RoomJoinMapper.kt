package pl.edu.pg.eti.data.mapper

import pl.edu.pg.eti.data.dto.RoomJoinDto
import pl.edu.pg.eti.domain.model.RoomJoin

fun RoomJoinDto.toDomain()=RoomJoin(room.name,queueName,room.hash,room.playersCount,room.maxPlayers,room.roundTime)