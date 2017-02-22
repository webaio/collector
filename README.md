Weba.IO Collector
==============

[![CircleCI](https://circleci.com/gh/webaio/collector/tree/master.svg?style=svg)](https://circleci.com/gh/webaio/collector/tree/master)

Collector is a part of Weba.IO application responsible for collecting incoming http events and pushing them to Kafka.

To run collector:

1. /gradlew clean
2. ./gradlew build
3. ./gradlew shadowJar
4. vertx run io.weba.collector.verticle.DeploymentVerticle -cp build/libs/io.weba.collector-fat.jar -conf resources/config.json