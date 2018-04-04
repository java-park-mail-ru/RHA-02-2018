package com.gameapi.rha.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {


    @Test
    @DisplayName("NullPassword check")
    void getSaltedHash() {
        assertThrows(IllegalArgumentException.class, ()->{Password.getSaltedHash(null);});
    }

    @Test
    @DisplayName("Check same passwords")
    void check() {

        assertEquals(true,Password.check("Strange pass",Password.getSaltedHash("Strange pass")));
    }

    @Test
    @DisplayName("Check different passwords")
    void checkqw() {

        assertEquals(false,Password.check("Strange pase",Password.getSaltedHash("Strange pass")));
    }
}