package com.sujai.adsquare.service;

import com.sujai.adsquare.exception.NotPlayerMoveException;
import com.sujai.adsquare.model.Board;
import com.sujai.adsquare.model.Game;
import com.sujai.adsquare.model.GameState;
import com.sujai.adsquare.model.PlayerType;
import com.sujai.adsquare.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    private GameRepository gameRepository;
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameRepository = Mockito.mock(GameRepository.class);
        gameService = new GameService(gameRepository);
    }

    private Game inProgressGame() {
        Game g = new Game();
        g.setId(1L);
        g.setState(GameState.IN_PROGRESS);
        g.setBoard(new Board());
        return g;
    }

    @Test
    void makeMove_successfulMove_updatesBoardAndSaves() {
        Game g = inProgressGame();
        when(gameRepository.findByIdAndState(1L, GameState.IN_PROGRESS)).thenReturn(Optional.of(g));

        String response = gameService.makeMove(1L, PlayerType.PLAYER1, 1);

        assertTrue(response.startsWith("SUCCESS:"));
        // Verify board slot set
        assertEquals(PlayerType.PLAYER1, g.getBoard().getSlot1());
        // Verify saved once
        verify(gameRepository, times(1)).save(g);
    }

    @Test
    void makeMove_invalidTurn_throwsNotPlayerMoveException() {
        Game g = inProgressGame();
        g.setLastMove(PlayerType.PLAYER1);
        when(gameRepository.findByIdAndState(1L, GameState.IN_PROGRESS)).thenReturn(Optional.of(g));

        assertThrows(NotPlayerMoveException.class, () -> gameService.makeMove(1L, PlayerType.PLAYER1, 1));
        verify(gameRepository, never()).save(any());
    }

    @Test
    void makeMove_winningMove_setsCompletedAndWinnerInResponse() {
        Game g = inProgressGame();
        // Set two in a slot for PLAYER1 at row 1, 2 then play 3
        g.getBoard().setSlot1(PlayerType.PLAYER1);
        g.getBoard().setSlot2(PlayerType.PLAYER1);
        when(gameRepository.findByIdAndState(1L, GameState.IN_PROGRESS)).thenReturn(Optional.of(g));

        String response = gameService.makeMove(1L, PlayerType.PLAYER1, 3);

        assertTrue(response.contains("Game completed"));
        assertEquals(GameState.COMPLETED, g.getState());
        assertEquals(PlayerType.PLAYER1, g.getWinner());
        verify(gameRepository).save(g);
    }

    @Test
    void makeMove_gameNotFound_throwsGameNotFound() {
        when(gameRepository.findByIdAndState(99L, GameState.IN_PROGRESS)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> gameService.makeMove(99L, PlayerType.PLAYER1, 1));
    }

    @Test
    void makeMove_gameAlreadyOver_throwsGameAlreadyOver() {
        Game g = inProgressGame();
        g.setState(GameState.COMPLETED);
        when(gameRepository.findByIdAndState(1L, GameState.IN_PROGRESS)).thenReturn(Optional.of(g));
        assertThrows(RuntimeException.class, () -> gameService.makeMove(1L, PlayerType.PLAYER1, 1));
    }
}
