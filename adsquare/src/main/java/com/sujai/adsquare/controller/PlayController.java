package com.sujai.adsquare.controller;

import com.sujai.adsquare.model.PlayerType;
import com.sujai.adsquare.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(
        path = "/api/v1/play",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class PlayController {
    private final GameService gameService;

    @PutMapping("{id}/{playerType}/{slot}")
    public ResponseEntity<String> makeMove(@PathVariable("id") Long id, @PathVariable PlayerType playerType, @PathVariable("slot") int slot) {
        return new ResponseEntity<>(gameService.makeMove(id, playerType, slot), HttpStatus.OK);
    }
}
