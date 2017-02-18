./gradlew build
./gradlew shadowJar
vertx run io.weba.collector.verticle.DeploymentVerticle -cp build/libs/io.weba.collector-fat.jar -conf resources/config.json