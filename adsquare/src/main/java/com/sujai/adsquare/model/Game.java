package com.sujai.adsquare.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "game")
@Getter
@Setter
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @CreatedDate  @Column(nullable = false) private LocalDateTime createdOn;
//    @CreatedBy private String createdBy;
//    @LastModifiedDate private LocalDateTime updateOn;
//    @LastModifiedBy private String updatedBy;

    private PlayerType lastMove;
    private PlayerType winner;

    private String player1Name;
    private String player2Name;

    private GameState state;
    private GameResult result;

    @OneToOne(cascade = CascadeType.ALL)
    private Board board;

    @Override
    public String toString() {
        return this.toString();
    }

    public boolean isMoveByPlayerValid(PlayerType playerType) {
        return playerType != lastMove;
    }


    public boolean isOver(PlayerType playerType) {
        boolean isWinnerFound = board.winnerFound(playerType);
        boolean isBoardFilled = board.isBoardFilled();

        if (isWinnerFound) {
            this.winner = playerType;
            this.state = GameState.COMPLETED;
            this.result = GameResult.WIN;
        } else if (isBoardFilled) {
            this.state = GameState.COMPLETED;
            this.result = GameResult.DRAW;
        }
        return isWinnerFound || isBoardFilled;
    }
}