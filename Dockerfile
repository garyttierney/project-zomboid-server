FROM steamcmd/steamcmd:ubuntu-20 AS pz_setup

ENV HOME=""
ENV USER=""

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y moreutils gettext

RUN steamcmd +force_install_dir /opt/project-zomboid  \
             +login anonymous \
             +app_update 380870 -beta b41multiplayer \
             +quit

FROM gradle:latest AS agent_build

ADD ./ /home/gradle/work/
WORKDIR /home/gradle/work
COPY --from=pz_setup /opt/project-zomboid /opt/project-zomboid
RUN ./gradlew -PpzDirectory=/opt/project-zomboid/java shadowJar
RUN mv /home/gradle/work/game-server-patcher/build/libs/pz-agent.jar /opt/project-zomboid

FROM ubuntu

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y gettext moreutils sqlite
RUN useradd -m -u 1000 zomboid-server
COPY --from=agent_build --chown=zomboid-server:zomboid-server /opt/project-zomboid /opt/project-zomboid
ADD --chown=zomboid-server:zomboid-server bin/configure-and-launch-pz /home/zomboid-server/
ADD --chown=zomboid-server:zomboid-server config/game /home/zomboid-server/Zomboid/
ADD --chown=zomboid-server:zomboid-server config/launcher /opt/project-zomboid/

USER zomboid-server
RUN curl -L https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.16.1/jmx_prometheus_javaagent-0.16.1.jar \
         -o /opt/project-zomboid/jmx_prometheus_javaagent.jar \
    && chmod +x /opt/project-zomboid/start-server.sh

EXPOSE 16261
EXPOSE 8766
EXPOSE 9099

WORKDIR /opt/project-zomboid
ENTRYPOINT ["/bin/sh", "/home/zomboid-server/configure-and-launch-pz"]
CMD ["/opt/project-zomboid/start-server.sh"]