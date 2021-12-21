package com.github.garyttierney.zomboid.server;

import com.github.garyttierney.zomboid.server.patches.PatchLoadMarker;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class ZomboidServerPatcher {
    public static void main(String[] argv) throws Exception {
        Instrumentation instr = ByteBuddyAgent.install();
        instrument(instr);
    }

    public static void instrument(Instrumentation inst) throws Exception {
        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.REDEFINITION)
                .with(AgentBuilder.Listener.StreamWriting.toSystemError().withTransformationsOnly())
                .with(AgentBuilder.InstallationListener.StreamWriting.toSystemError())
                .type(nameContains("IntegerServerOption"))
                .transform(
                        new AgentBuilder.Transformer.ForAdvice()
                                .include(PatchLoadMarker.class.getClassLoader())
                                .advice(isConstructor(), "com.github.garyttierney.zomboid.server.patches.ChangeServerOptionDefaultsAdvice")
                )
                .type(named("zombie.network.GameServer"))
                .transform(
                        new AgentBuilder.Transformer.ForAdvice()
                                .include(PatchLoadMarker.class.getClassLoader())
                                .advice(named("main"), "com.github.garyttierney.zomboid.server.patches.GameServerStartupAdvice")
                )
                .type(named("zombie.network.ServerOptions"))
                .transform(
                        new AgentBuilder.Transformer.ForAdvice()
                                .include(PatchLoadMarker.class.getClassLoader())
                                .advice(named("getMaxPlayers"), "com.github.garyttierney.zomboid.server.patches.ChangeServerOptionsGetMaxPlayerAdvice")
                )
                .installOn(inst);
    }

    public static void premain(String arg, Instrumentation inst) throws Exception {
        instrument(inst);
    }
}
