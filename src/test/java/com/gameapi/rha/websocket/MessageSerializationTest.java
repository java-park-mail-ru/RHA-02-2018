//package com.gameapi.rha.websocket;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.gameapi.rha.mechanics.messages.input.JoinGame;
//
//import
//import java.io.IOException;
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//class MessageSerializationTest {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @ParameterizedTest
//    @MethodSource("twoWaySerializableProvider")
//    void test2WaySerializable(Message message) throws IOException {
//        final String requestJson = objectMapper.writeValueAsString(message);
//        final Message fromJson = objectMapper.readValue(requestJson, Message.class);
//        assertEquals(message.getClass(), fromJson.getClass());
//    }
//
//    @ParameterizedTest
//    @MethodSource("oneWaySerializableProvider")
//    void test1WaySerializable(Message message) throws IOException {
//        final String messageJson = objectMapper.writeValueAsString(message);
//        assertNotNull(messageJson);
//    }
//
//    private static Stream<Message> twoWaySerializableProvider() {
//        final JoinGame.Request joinRequest = new JoinGame.Request();
//        final ClientSnap clientSnap = new ClientSnap();
//        clientSnap.setMouse(Coords.of(0, 0));
//        final FinishGame finishGame = new FinishGame(FinishGame.Overcome.DRAW);
//
//        return Stream.of(joinRequest,
//                clientSnap,
//                finishGame);
//
//    }
//
//    private static Stream<Message> oneWaySerializableProvider() {
//        final InitGame.Request initGame = new InitGame.Request();
//        initGame.setBoard(new Board.BoardSnap(List.of(), Id.of(0L), List.of()));
//        initGame.setColors(Map.of());
//        initGame.setEnemy(Id.of(0));
//        initGame.setSelf(Id.of(1));
//        initGame.setNames(Map.of());
//        initGame.setPlayers(Map.of());
//        final ServerSnap serverSnap = new ServerSnap();
//        serverSnap.setBoard(new Board.BoardSnap(List.of(), Id.of(0L), List.of()));
//        serverSnap.setPlayers(List.of());
//
//        return Stream.of(initGame,
//                serverSnap);
//    }
//}