package org.intuit.scoreservice.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intuit.scoreservice.models.entity.Player;
import org.intuit.scoreservice.repositories.PlayerRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Lazy
@Slf4j
public class PlayerService {

    private final PlayerRepository playerRepository;

    public void createPlayer(Player player) {
        playerRepository.save(player);
        log.info("created playerId" + player.getId() + "for" + player.getUsername());
    }
}
