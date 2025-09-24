package com.sujai.adsquare.controller;

import com.sujai.adsquare.model.Game;
import com.sujai.adsquare.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
@RequestMapping(
        path = "/api/v1/game",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class GameController {
    private final GameService gameService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Game> createNewGame(@org.springframework.web.bind.annotation.RequestBody java.util.Map<String, String> body) {
        String player1Name = body.get("player1Name");
        String player2Name = body.get("player2Name");
        return new ResponseEntity<>(gameService.createNewGame(player1Name, player2Name), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Game> getGameInfo(@PathVariable("id") Long id) {
        return new ResponseEntity<>(gameService.getGameInfo(id), HttpStatus.OK);
    }
}
