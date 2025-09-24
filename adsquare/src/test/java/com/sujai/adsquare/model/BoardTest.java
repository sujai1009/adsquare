package com.sujai.adsquare.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void isRowEmptyThenSet_setsOnlyWhenEmpty_andReturnsFlags() {
        Board b = new Board();
        assertTrue(b.isSlotEmptyThenSet(1, PlayerType.PLAYER1));
        assertEquals(PlayerType.PLAYER1, b.getSlot1());
        // second attempt on same row should return false and not change
        assertFalse(b.isSlotEmptyThenSet(1, PlayerType.PLAYER2));
        assertEquals(PlayerType.PLAYER1, b.getSlot1());
    }

    @Test
    void winnerFound_rows_columns_diagonals() {
        // row one
        Board b1 = new Board();
        b1.setSlot1(PlayerType.PLAYER1);
        b1.setSlot2(PlayerType.PLAYER1);
        b1.setSlot3(PlayerType.PLAYER1);
        assertTrue(b1.winnerFound(PlayerType.PLAYER1));

        // column two
        Board b2 = new Board();
        b2.setSlot2(PlayerType.PLAYER2);
        b2.setSlot5(PlayerType.PLAYER2);
        b2.setSlot8(PlayerType.PLAYER2);
        assertTrue(b2.winnerFound(PlayerType.PLAYER2));

        // diagonal right (7,5,3)
        Board b3 = new Board();
        b3.setSlot7(PlayerType.PLAYER1);
        b3.setSlot5(PlayerType.PLAYER1);
        b3.setSlot3(PlayerType.PLAYER1);
        assertTrue(b3.winnerFound(PlayerType.PLAYER1));
    }
}
