package com.binar.binarch4.model

import com.binar.binarch4.enum.PlayerSide
import com.binar.binarch4.enum.PlayerState
import com.binar.binarch4.enum.PlayerType

data class Player(
    val playerSide: PlayerSide,
    var playerState: PlayerState,
    var playerType: PlayerType
)