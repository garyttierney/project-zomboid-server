package com.github.garyttierney.zomboid.server.api

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.http.Context

class ZomboidApiServer(private val adapter: ZomboidApiServerAdapter) {

    private val app = Javalin.create()

    fun stop() = app.stop()
    fun start() = app.start()

    init {
        app.routes {
            get("/info", ::serverInfoGet)
        }
    }

    fun serverInfoGet(ctx: Context) {
        ctx.json(
            ZomboidServerInfo(
                players = PlayerInfo(
                    current = adapter.players.size,
                    max = adapter.options.maxPlayers
                ),
                world = WorldInfo(
                    daysElapsed = adapter.world.worldAgeDays,
                    weather = adapter.world.weather
                )
            )
        )
    }
}