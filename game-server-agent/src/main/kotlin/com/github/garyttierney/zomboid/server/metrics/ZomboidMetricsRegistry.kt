package com.github.garyttierney.zomboid.server.metrics

import io.micrometer.core.instrument.binder.jvm.*
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry

object ZomboidMetricsRegistry : PrometheusMeterRegistry(PrometheusConfig.DEFAULT) {
    init {
        JvmGcMetrics().bindTo(this)
        JvmCompilationMetrics().bindTo(this)
        JvmHeapPressureMetrics().bindTo(this)
        JvmMemoryMetrics().bindTo(this)
        ZomboidWorldMetrics().bindTo(this)
    }
}