package com.github.garyttierney.zomboid.server.metrics

import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.MeterBinder
import zombie.iso.IsoWorld
import zombie.network.GameServer
import zombie.network.ServerMap

class ZomboidWorldMetrics : MeterBinder {
    override fun bindTo(registry: MeterRegistry) {
        Gauge
            .builder("zomboid.world.players") {
                if (GameServer.udpEngine != null) 0 else GameServer.getPlayerCount()
            }
            .description("The number of players currently connected and in the world")
            .register(registry)

        Gauge
            .builder("zomboid.world.zombies.loaded") {
                IsoWorld.instance.cell.zombieList.size
            }
            .description("The number of zombies currently loaded in the world")
            .register(registry)

        Gauge
            .builder("zomboid.world.zombies.total") {
                ServerMap.instance.ZombieMap.size()
            }
            .description("The total number of zombies that exist in the world")
            .register(registry)
    }
}