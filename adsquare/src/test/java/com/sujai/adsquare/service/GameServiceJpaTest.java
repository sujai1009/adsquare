package com.sujai.adsquare.service;

import com.sujai.adsquare.exception.NotPlayerMoveException;
import com.sujai.adsquare.model.*;
import com.sujai.adsquare.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class GameServiceJpaTest {
    @Autowired private GameRepository gameRepository;
    @Autowired private GameService gameService;

    @BeforeEach
    public void setup() {
        Game g = new Game();
        g.setPlayer1Name("Alice");
        g.setPlayer2Name("Bob");
        g.setState(GameState.IN_PROGRESS);
        g.setBoard(new Board());
        gameRepository.save(g);
    }

    @Test
    void testCreateNewGame() {
        Game createdGame = gameService.createNewGame("Alice", "Bob");
        Game g = gameRepository.findById(createdGame.getId()).get();
        assertNotNull(g);
        assertNotNull(g.getBoard());
        assertEquals(GameState.IN_PROGRESS, g.getState());
        assertEquals("Alice", g.getPlayer1Name());
        assertEquals("Bob", g.getPlayer2Name());
    }

    @Test
    void getGameInfo_ok() {
        Game g = gameRepository.findById(1L).get();
        assertNotNull(g);
        assertNotNull(g.getBoard());
    }

    @Test
    void testPlayGame_Completed_Player1Wins() {
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER1, 1));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER2, 2));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER1, 3));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER2, 4));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER1, 5));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER2, 6));
        assertEquals(GameService.RESPONSE + "Game completed. Winner is PLAYER1", gameService.makeMove(1L, PlayerType.PLAYER1, 7));

        Game g = gameRepository.findById(1L).get();
        assertEquals(GameState.COMPLETED, g.getState());
        assertEquals(GameResult.WIN, g.getResult());
        assertEquals(PlayerType.PLAYER1, g.getWinner());
    }

    @Test
    void testPlayGame_Completed_inDraw() {
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER1, 1));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER2, 2));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER1, 3));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER2, 5));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER1, 4));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER2, 6));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER1, 8));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER2, 7));
        assertEquals(GameService.RESPONSE + "Game completed in draw", gameService.makeMove(1L, PlayerType.PLAYER1, 9));

        Game g = gameRepository.findById(1L).get();
        assertEquals(GameState.COMPLETED, g.getState());
        assertEquals(GameResult.DRAW, g.getResult());
    }

    @Test
    void testPlayGame_Throw_NotPlayerMoveException() {
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER1, 1));
        assertThrows(NotPlayerMoveException.class, () -> {
            gameService.makeMove(1L, PlayerType.PLAYER1, 2);
        });
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER2, 2));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER1, 3));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER2, 4));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER1, 5));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER2, 6));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER1, 8));
        assertEquals(GameService.RESPONSE + "Move is completed", gameService.makeMove(1L, PlayerType.PLAYER2, 7));

        assertThrows(NotPlayerMoveException.class, () -> {
            gameService.makeMove(1L, PlayerType.PLAYER2, 9);
        });

        Game g = gameRepository.findById(1L).get();
        assertEquals(GameState.IN_PROGRESS, g.getState());
        assertNull(g.getResult());
    }
}
