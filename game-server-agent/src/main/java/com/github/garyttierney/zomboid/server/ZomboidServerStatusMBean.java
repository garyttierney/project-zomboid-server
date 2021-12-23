package com.github.garyttierney.zomboid.server;

public interface ZomboidServerStatusMBean {
    Integer getCurrentPlayers();
    Integer getTotalZombies();
    Integer getLoadedZombies();
    Long getTotalTicks();
}
