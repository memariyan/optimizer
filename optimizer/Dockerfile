######  #     #   ###   #       ######           #####  #######    #     #####  #######
#     # #     #    #    #       #     #         #     #    #      # #   #       #
#     # #     #    #    #       #     #         #          #     #   #  #       #
######  #     #    #    #       #     #          #####     #    #     # #  #### #####
#     # #     #    #    #       #     #               #    #    ####### #     # #
#     # #     #    #    #       #     #         #     #    #    #     # #     # #
######   #####    ###   ####### ######           #####     #    #     #  ####   #######
FROM base-maven:3.9.6-eclipse-temurin-21 AS builder


ARG SKIP_TEST=true

WORKDIR /build

COPY ./checkstyle.xml .
COPY ./pom.xml .
COPY ./src ./src

# Build application
RUN mvn -q -s /usr/share/maven/ref/settings.xml -Dmaven.test.skip=true package

######  #     # #     #          #####  #######    #     #####  #######
#     # #     # ##    #         #     #    #      # #   #     # #
#     # #     # # #   #         #          #     #   #  #       #
######  #     # #  #  #          #####     #    #     # #  #### #####
#   #   #     # #   # #               #    #    ####### #     # #
#    #  #     # #    ##         #     #    #    #     # #     # #
#     #  #####  #     #          #####     #    #     #  #####  #######
FROM base-maven:3.9.6-eclipse-temurin-21


WORKDIR /

RUN cp /usr/share/zoneinfo/Asia/Tehran /etc/localtime && \
    echo "Asia/Tehran" > /etc/timezone

# Add application built jar file
COPY --from=builder /build/target/*.jar app.jar

# COPY and give execute permission for startup.sh
COPY startup.sh startup.sh
RUN chmod +x startup.sh

# Environments
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    START_SLEEP=0 \
    JAVA_OPTS="" \

# Expose port
EXPOSE=8050

# Execute command
CMD './startup.sh'
