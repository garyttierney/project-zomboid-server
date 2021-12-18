package com.github.garyttierney.zomboid.server.patches;

import net.bytebuddy.asm.Advice;
import zombie.network.ServerOptions;

public class ChangeServerOptionsGetMaxPlayerAdvice {
    @Advice.OnMethodExit
    public static void after(@Advice.Return(readOnly = false) int returned) {
        returned = ServerOptions.getInstance().MaxPlayers.getValue();
    }
}
