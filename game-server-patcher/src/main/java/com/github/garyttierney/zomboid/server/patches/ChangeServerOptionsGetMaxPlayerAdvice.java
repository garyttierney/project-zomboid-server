package com.github.garyttierney.zomboid.server.patches;

import net.bytebuddy.asm.Advice;
import zombie.network.ServerOptions;

import static net.bytebuddy.implementation.bytecode.assign.Assigner.Typing.DYNAMIC;

public class ChangeServerOptionsGetMaxPlayerAdvice {
    @Advice.OnMethodExit
    public static void after(@Advice.Return(readOnly = false) int returned) {
        returned = ServerOptions.getInstance().MaxPlayers.getValue();
    }
}
