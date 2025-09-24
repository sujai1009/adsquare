package com.sujai.adsquare.controller;

import com.sujai.adsquare.model.PlayerType;
import com.sujai.adsquare.service.GameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayController.class)
class PlayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    @DisplayName("PUT /api/v1/play/{id}/{playerType}/{slot} returns 200 on success")
    void makeMove_ok() throws Exception {
        when(gameService.makeMove(1L, PlayerType.PLAYER1, 3)).thenReturn("SUCCESS:Move is completed");

        mockMvc.perform(put("/api/v1/play/{id}/{playerType}/{slot}", 1L, "PLAYER1", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
