package com.sujai.adsquare.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "board")
@Getter
@Setter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private PlayerType slot1;
    private PlayerType slot2;
    private PlayerType slot3;
    private PlayerType slot4;
    private PlayerType slot5;
    private PlayerType slot6;
    private PlayerType slot7;
    private PlayerType slot8;
    private PlayerType slot9;


    @Override
    public String toString() {
        return this.toString();
    }

    private boolean isSlotEmpty(int slot) {
        return switch (slot) {
            case 1 -> slot1 == null;
            case 2 -> slot2 == null;
            case 3 -> slot3 == null;
            case 4 -> slot4 == null;
            case 5 -> slot5 == null;
            case 6 -> slot6 == null;
            case 7 -> slot7 == null;
            case 8 -> slot8 == null;
            case 9 -> slot9 == null;
            default -> false;
        };
    }
    public boolean isSlotEmptyThenSet(int slot, PlayerType playerType) {
        boolean isSlotEmpty = isSlotEmpty(slot);
        if(isSlotEmpty) {
            setSlot(slot, playerType);
        }
        return isSlotEmpty;
    }

    public void setSlot(int slot, PlayerType playerType) {
        switch (slot) {
            case 1 -> slot1 = playerType;
            case 2 -> slot2 = playerType;
            case 3 -> slot3 = playerType;
            case 4 -> slot4 = playerType;
            case 5 -> slot5 = playerType;
            case 6 -> slot6 = playerType;
            case 7 -> slot7 = playerType;
            case 8 -> slot8 = playerType;
            case 9 -> slot9 = playerType;
        }
    }

    public boolean isBoardFilled() {
        return !isSlotEmpty(1) && !isSlotEmpty(2) && !isSlotEmpty(3) && !isSlotEmpty(4) && !isSlotEmpty(5) && !isSlotEmpty(6) && !isSlotEmpty(7) && !isSlotEmpty(8) && !isSlotEmpty(9);
    }

    public boolean winnerFound(PlayerType playerType) {
        boolean isRowFilled = rowOne(playerType) || rowTwo(playerType) || rowThree(playerType);
        boolean isColumnFilled = columnOne(playerType) || columnTwo(playerType) || columnThree(playerType);
        boolean isDiagonalFilled = diagonalLeft(playerType) || diagonalRight(playerType);

        return isRowFilled || isColumnFilled || isDiagonalFilled;
    }

    private boolean rowOne(PlayerType playerType) {
        return slot1 == playerType && slot2 == playerType && slot3 == playerType;
    }

    private boolean rowTwo(PlayerType playerType) {
        return slot4 == playerType && slot5 == playerType && slot6 == playerType;
    }

    private boolean rowThree(PlayerType playerType) {
        return slot7 == playerType && slot8 == playerType && slot9 == playerType;
    }

    private boolean columnOne(PlayerType playerType) {
        return slot1 == playerType && slot4 == playerType && slot7 == playerType;
    }

    private boolean columnTwo(PlayerType playerType) {
        return slot2 == playerType && slot5 == playerType && slot8 == playerType;
    }

    private boolean columnThree(PlayerType playerType) {
        return slot3 == playerType && slot6 == playerType && slot9 == playerType;
    }

    private boolean diagonalLeft(PlayerType playerType) {
        return slot1 == playerType && slot5 == playerType && slot9 == playerType;
    }

    private boolean diagonalRight(PlayerType playerType) {
        return slot7 == playerType && slot5 == playerType && slot3 == playerType;
    }
}
