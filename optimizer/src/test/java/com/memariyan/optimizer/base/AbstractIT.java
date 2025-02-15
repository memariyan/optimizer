package com.memariyan.optimizer.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
@TestPropertySource({"classpath:/application-test.properties"})
public class AbstractIT extends EmbeddedIT {

}
