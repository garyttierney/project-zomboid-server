package com.github.garyttierney.zomboid.server.patches;

import net.bytebuddy.asm.Advice;

public class ChangeServerOptionDefaultsAdvice {
    @Advice.OnMethodEnter
    public static void before(@Advice.Argument(value = 1) String name,
                              @Advice.Argument(value = 3, readOnly = false) int max) {
        if (name.equals("MaxPlayers")) {
            max = 512;
            System.out.println("[pz-agent] patched MaxPlayer limit to 512");
        }
    }
}
