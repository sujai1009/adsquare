package com.sujai.adsquare.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void isMoveByPlayerValid_enforcesAlternateTurns() {
        Game game = new Game();
        // No last move, both players should be valid in this simplistic rule
        assertTrue(game.isMoveByPlayerValid(PlayerType.PLAYER1));
        assertTrue(game.isMoveByPlayerValid(PlayerType.PLAYER2));

        game.setLastMove(PlayerType.PLAYER1);
        assertFalse(game.isMoveByPlayerValid(PlayerType.PLAYER1));
        assertTrue(game.isMoveByPlayerValid(PlayerType.PLAYER2));
    }

    @Test
    void isOver_delegatesToBoard() {
        Game game = new Game();
        Board board = new Board();
        game.setBoard(board);
        // set a winning diagonal
        board.setSlot1(PlayerType.PLAYER1);
        board.setSlot5(PlayerType.PLAYER1);
        board.setSlot9(PlayerType.PLAYER1);

        assertTrue(game.isOver(PlayerType.PLAYER1));
        assertFalse(game.isOver(PlayerType.PLAYER2));
    }
}
