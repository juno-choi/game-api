FROM openjdk:11-ea-27-jdk-slim

VOLUME /tmp

# jar파일 복사
COPY build/libs/game-api-1.0.jar game.jar
ENTRYPOINT ["java","-jar","game.jar"]