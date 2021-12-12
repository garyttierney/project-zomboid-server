FROM steamcmd/steamcmd:ubuntu-20

ENV HOME=""
ENV USER=""

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y moreutils gettext

RUN steamcmd +force_install_dir /opt/project-zomboid  \
             +login anonymous \
             +app_update 380870 -beta b41multiplayer \
             +quit

RUN useradd -u 1000 -m zomboid-server && \
    chown -R zomboid-server:zomboid-server /opt/project-zomboid

USER zomboid-server
WORKDIR /home/zomboid-server
ADD --chown=zomboid-server:zomboid-server bin/configure-and-launch-pz /home/zomboid-server/
ADD --chown=zomboid-server:zomboid-server config/game /home/zomboid-server/Zomboid/
ADD --chown=zomboid-server:zomboid-server config/launcher /opt/project-zomboid/
RUN curl -L https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.16.1/jmx_prometheus_javaagent-0.16.1.jar \
         -o /opt/project-zomboid/jmx_prometheus_javaagent.jar
EXPOSE 16261
EXPOSE 8766

WORKDIR /opt/project-zomboid
ENTRYPOINT ["/bin/bash", "/home/zomboid-server/configure-and-launch-pz"]
CMD ["/opt/project-zomboid/start-server.sh"]