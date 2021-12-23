package com.github.garyttierney.zomboid.server.patches;

import com.github.garyttierney.zomboid.server.ZomboidServerStatus;
import com.github.garyttierney.zomboid.server.api.DefaultZomboidApiServerAdapter;
import com.github.garyttierney.zomboid.server.api.ZomboidApiServer;
import net.bytebuddy.asm.Advice;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameServerStartupAdvice {
    public static final ZomboidApiServer server = new ZomboidApiServer(new DefaultZomboidApiServerAdapter());
    public static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Advice.OnMethodEnter
    public static void before() {
        server.start();

        try {
            MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName objectName = new ObjectName("com.projectzomboid:name=ZomboidServerStatus");

            ZomboidServerStatus statusMBean = new ZomboidServerStatus();
            executor.scheduleAtFixedRate(statusMBean::update, 0L, 600L, TimeUnit.MILLISECONDS);

            platformMBeanServer.registerMBean(statusMBean, objectName);
        } catch (Exception e) {
            System.out.println("[pz-agent] unable to register metrics MBean");
        }

    }

    @Advice.OnMethodExit
    public static void after() {
        server.stop();
        executor.shutdown();
    }
}
