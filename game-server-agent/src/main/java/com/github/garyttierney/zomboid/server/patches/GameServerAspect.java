package com.github.garyttierney.zomboid.server.patches;

import com.github.garyttierney.zomboid.server.api.DefaultZomboidApiServerAdapter;
import com.github.garyttierney.zomboid.server.api.ZomboidApiServer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class GameServerAspect {
    public static final ZomboidApiServer apiServer = new ZomboidApiServer(new DefaultZomboidApiServerAdapter());

    @Around("execution (public static void zombie.network.GameServer.main(String[]))")
    public Object beforeAdvice(ProceedingJoinPoint pjp) throws Throwable {
        try {
            apiServer.start();
            return pjp.proceed();
        } finally {
            apiServer.stop();
        }
    }
}
