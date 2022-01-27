package com.github.garyttierney.zomboid.server.api

import com.github.garyttierney.zomboid.server.metrics.ZomboidMetricsRegistry
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.http.Context
import io.javalin.plugin.metrics.MicrometerPlugin
import io.prometheus.client.exporter.common.TextFormat


class ZomboidApiServer(private val adapter: ZomboidApiServerAdapter) {
    private val app: Javalin = Javalin.create() {
        it.registerPlugin(MicrometerPlugin(ZomboidMetricsRegistry))
    }

    fun stop() = app.stop()
    fun start() = app.start(8080)

    init {
        app.routes {
            get("/info", ::serverInfoGet)
            get("/metrics", ::metricsGet)
        }
    }

    fun metricsGet(ctx: Context) {
        ctx.apply {
            contentType(TextFormat.CONTENT_TYPE_004)
            result(ZomboidMetricsRegistry.scrape())
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