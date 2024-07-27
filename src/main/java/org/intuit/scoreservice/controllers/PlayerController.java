package org.intuit.scoreservice.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intuit.scoreservice.models.entity.Player;
import org.intuit.scoreservice.models.dto.PlayerDTO;
import org.intuit.scoreservice.services.PlayerService;
import org.intuit.scoreservice.validators.PlayerValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/players")
public class PlayerController {
    @Autowired
    private final PlayerService playerService;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final PlayerValidator playerValidatorImpl;

    @PostMapping
    public ResponseEntity<Void> createPlayer(@RequestBody PlayerDTO playerDTO) {
        try {
            Player player = modelMapper.map(playerDTO, Player.class);
            playerValidatorImpl.validate(player);
            playerService.createPlayer(player);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
