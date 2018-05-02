package com.gameapi.rha;


import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Executors;

/**
 * Created by Solovyev on 06/11/2016.
 */
@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    @NotNull
    private final WebSocketHandler webSocketHandler;

    public WebSocketConfig(@NotNull WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(@NotNull WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/multiplayer/rand")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*");
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor());
    }

}