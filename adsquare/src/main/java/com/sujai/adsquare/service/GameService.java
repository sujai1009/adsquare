package com.sujai.adsquare.service;

import com.sujai.adsquare.exception.GameAlreadyOverException;
import com.sujai.adsquare.exception.GameNotFoundException;
import com.sujai.adsquare.exception.NotPlayerMoveException;
import com.sujai.adsquare.model.*;
import com.sujai.adsquare.repository.GameRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GameService {
    public static final String RESPONSE = "SUCCESS:";
    private final GameRepository gameRepository;
    public Game getGameInfo(Long gameId) {
        log.info("Fetching game info for game id: {}", gameId);
        return gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game not found with id" + gameId));
    }

    public Game createNewGame(String player1Name, String player2Name) {
        log.info("Creating new game for player1: {} and player2: {}", player1Name, player2Name);
        Game game = new Game();
        game.setPlayer1Name(player1Name);
        game.setPlayer2Name(player2Name);
        game.setState(GameState.IN_PROGRESS);
        game.setBoard(new Board());
        return gameRepository.save(game);
    }

    public String makeMove(Long gameId, PlayerType playerType, int slot) {
        Optional<Game> game = gameRepository.findByIdAndState(gameId, GameState.IN_PROGRESS);

        game.ifPresentOrElse(g -> {
            if (g.getState() != GameState.IN_PROGRESS) {
                throw new GameAlreadyOverException("Game already over with id:" + gameId);
            }
            if (!g.isMoveByPlayerValid(playerType)) {
                throw new NotPlayerMoveException("Not a valid move by plapyer:" + playerType);
            }
            boolean isValidMove = g.getBoard().isSlotEmptyThenSet(slot, playerType);
            boolean iGameOver = g.isOver(playerType);
            if(isValidMove && iGameOver) {
                g.setState(GameState.COMPLETED);
                g.setWinner(playerType);
            } else {
                g.setLastMove(playerType);
            }
            gameRepository.save(g);
        }, () -> {
            new GameNotFoundException("Game not found with id:" + gameId);
        });
        if (game.get().getState() == GameState.COMPLETED && game.get().getResult() == GameResult.WIN) {
            return RESPONSE + "Game completed. Winner is " + game.get().getWinner();
        } else if (game.get().getResult() == GameResult.DRAW) {
            return RESPONSE + "Game completed in draw";
        }else {
            return RESPONSE + "Move is completed";
        }
    }
}
