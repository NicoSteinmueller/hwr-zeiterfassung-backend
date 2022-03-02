package com.hwr.hwrzeiterfassung.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class HelloWorldControllerTest {

    @Test
    void checkHelloWorld() {

        assertEquals("Hello World", new HelloWorldController().helloWorld());
    }
}
