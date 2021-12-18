package com.github.garyttierney.zomboid.server.api

data class PlayerInfo(val current: Int, val max: Int)
data class WorldInfo(val daysElapsed: Float, val weather: String)

data class ZomboidServerInfo(val players: PlayerInfo, val world: WorldInfo)