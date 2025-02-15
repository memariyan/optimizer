package com.memariyan.optimizer.base;

import com.memariyan.components.test.annotation.EnableTestContainers;
import com.memariyan.components.test.annotation.ImageType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@EnableTestContainers(images = {ImageType.MONGO, ImageType.KAFKA, ImageType.WIREMOCK, ImageType.REDIS})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DockerizedIT {

    @DynamicPropertySource
    public static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.autoconfigure.exclude",
                () -> "de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration");
    }

}
