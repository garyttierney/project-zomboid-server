package com.github.garyttierney.zomboid.server.patches;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import zombie.network.ServerOptions;

@Aspect
public class ServerOptionsAspect {
    @Around("initialization (zombie.network.ServerOptions.IntegerServerOption(..))")
    public Object initializeIntegerOption(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();

        if ("MaxPlayers".equals(args[1])) {
            args[3] = 512;
        }

        return pjp.proceed(args);
    }

    @Around("execution (public int zombie.network.ServerOptions.getMaxPlayers())")
    public Object removeMaxPlayerLimit(ProceedingJoinPoint pjp) throws Throwable {
        return ServerOptions.getInstance().MaxPlayers.getValue();
    }
}
