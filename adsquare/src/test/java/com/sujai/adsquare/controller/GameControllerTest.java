package com.sujai.adsquare.controller;

import com.sujai.adsquare.model.Game;
import com.sujai.adsquare.model.GameState;
import com.sujai.adsquare.service.GameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    @DisplayName("GET /api/v1/game/{id} returns 200 when service returns game")
    void getGameInfo_ok() throws Exception {
        Game g = new Game();
        g.setId(42L);
        g.setState(GameState.IN_PROGRESS);
        when(gameService.getGameInfo(42L)).thenReturn(g);

        mockMvc.perform(get("/api/v1/game/{id}", 42L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/v1/game returns 201 and body from service createNewGame")
    void createNewGame_ok() throws Exception {
        Game created = new Game();
        created.setId(100L);
        created.setPlayer1Name("Alice");
        created.setPlayer2Name("Bob");
        created.setState(GameState.IN_PROGRESS);
        when(gameService.createNewGame(anyString(), anyString())).thenReturn(created);

        String body = "{\"player1Name\":\"Alice\",\"player2Name\":\"Bob\"}";

        mockMvc.perform(post("/api/v1/game")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.player1Name", is("Alice")))
                .andExpect(jsonPath("$.player2Name", is("Bob")))
                .andExpect(jsonPath("$.state", is("IN_PROGRESS")));
    }
}
