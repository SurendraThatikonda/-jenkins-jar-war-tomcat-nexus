package com.surendra;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
class HelloJenkinsTest {
    @Test void returnsExpectedMessage() {
        assertEquals("Hello from the JAR module — built by Jenkins!", new HelloJenkins().message());
    }
}
