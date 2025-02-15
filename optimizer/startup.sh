#!/usr/bin/env sh

echo "The application will start..." && \
    sleep ${START_SLEEP} && \
    java ${JAVA_OPTS} ${OPTIONS} -Dsun.misc.URLClassPath.disableJarChecking=true \
                      -Djava.io.tmpdir=/var/tmp \
                      -Djava.security.egd=file:/dev/./urandom \
                      -jar ./app.jar
