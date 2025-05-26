package com.springboot.sensor;

import org.junit.jupiter.api.*;

public class TestLifeCycle {

    @BeforeAll
    static void beforeAll() {
        System.out.println("## beforeAll 어노테이션 호출 ##");
        System.out.println();
    }

    @AfterAll
    static void afterAll() {
        System.out.println("##afterAll 어노테이션 호출 ##");
        System.out.println();
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("## beforeEach 어노테이션 호출 ##");
        System.out.println();
    }

    @AfterEach
    void afterEach() {
        System.out.println("## afterEach 어노테이션 호출 ##");
        System.out.println();
    }
    @Test
    void testOne() {
        System.out.println("## testOne ##");
        System.out.println();
    }
    @Test
    @DisplayName("TestCase two")
    void testTwo() {
        System.out.println("## testTwo ##");
        System.out.println();
    }

    @Test
    @Disabled
    void testThree() {
        System.out.println("## testThree ##");
        System.out.println();
    }



}
