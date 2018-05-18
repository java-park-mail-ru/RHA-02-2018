package com.gameapi.rha.mechanics.game.resources;

import com.gameapi.rha.mechanics.game.Hex;
import com.gameapi.rha.mechanics.services.ResourceFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ResourceFactoryTest {


    @Autowired
    private ResourceFactory resourceFactory;


    @Test
    void readMap() {
        List<Hex> map= resourceFactory.readMap("maps/trainingMap");
        assertNotNull(map);
    }
}