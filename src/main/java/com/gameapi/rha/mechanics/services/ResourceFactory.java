package com.gameapi.rha.mechanics.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameapi.rha.mechanics.game.Hex;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Service
public class ResourceFactory {

    private final ObjectMapper objectMapper;
    private final Path basePath;

    public ResourceFactory(ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
        this.basePath = Paths.get("src/main/resources");
    }


    private static class ListTypeReference extends TypeReference<List<List<Hex>>> {
    }

    public List<List<Hex>> readMap(String mapPath) {
        try {
            File some = basePath.resolve(Paths.get(mapPath)).toRealPath().toFile();
            if (some.exists()) {
                System.out.println(some.canRead());
            }
            return objectMapper.readValue(some, new ListTypeReference());
        } catch (IOException e) {
            throw new ResourceException("Failed reading json by path " + basePath + mapPath, e);
        }
    }

}
