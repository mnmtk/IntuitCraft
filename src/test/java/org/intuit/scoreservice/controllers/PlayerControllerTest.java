package org.intuit.scoreservice.controllers;

import org.intuit.scoreservice.controllers.PlayerController;
import org.intuit.scoreservice.models.dto.PlayerDTO;
import org.intuit.scoreservice.models.entity.Player;
import org.intuit.scoreservice.services.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PlayerControllerTest {
    @Mock
    private PlayerService playerService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PlayerController playerController;

    @Test
    void testCreatePlayer() {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setUsername("testuser");

        Player player = new Player();
        player.setUsername("testuser");

        Mockito.when(modelMapper.map(playerDTO, Player.class)).thenReturn(player);

        ResponseEntity<Void> response = playerController.createPlayer(playerDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Mockito.verify(playerService).createPlayer(player);
    }
}
