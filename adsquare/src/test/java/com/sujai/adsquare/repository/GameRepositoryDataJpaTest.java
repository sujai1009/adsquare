package com.sujai.adsquare.repository;

import com.sujai.adsquare.model.Board;
import com.sujai.adsquare.model.Game;
import com.sujai.adsquare.model.GameState;
import com.sujai.adsquare.model.PlayerType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class GameRepositoryDataJpaTest {

    @Autowired
    private GameRepository gameRepository;

    @Test
    void saveAndFindByIdAndState_withH2_shouldReturnSavedEntity() {
        // Given
        Game game = new Game();
        game.setPlayer1Name("Alice");
        game.setPlayer2Name("Bob");
        game.setState(GameState.IN_PROGRESS);
        Board board = new Board();
        // make one move so some persistence of enum happens
        board.setSlot(1, PlayerType.PLAYER1);
        game.setBoard(board);

        // When
        Game saved = gameRepository.save(game);
        assertNotNull(saved.getId(), "Saved game must have id");

        // Then
        Optional<Game> found = gameRepository.findByIdAndState(saved.getId(), GameState.IN_PROGRESS);
        assertTrue(found.isPresent(), "Should find game by id and state IN_PROGRESS");
        Game g = found.get();
        assertEquals("Alice", g.getPlayer1Name());
        assertEquals("Bob", g.getPlayer2Name());
        assertNotNull(g.getBoard(), "Board should be persisted and loaded");
        assertEquals(PlayerType.PLAYER1, g.getBoard().getSlot1());
    }
}
