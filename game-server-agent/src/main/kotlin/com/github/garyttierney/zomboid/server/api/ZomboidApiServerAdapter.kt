package com.github.garyttierney.zomboid.server.api

import zombie.characters.IsoPlayer
import zombie.iso.IsoWorld
import zombie.network.GameServer
import zombie.network.ServerOptions

interface ZomboidApiServerAdapter {
    val players: List<IsoPlayer>
    val options: ServerOptions
    val world: IsoWorld
}

class DefaultZomboidApiServerAdapter : ZomboidApiServerAdapter {
    override val players get() = GameServer.getPlayers()
    override val options get() = ServerOptions.getInstance()
    override val world get() = IsoWorld.instance
}