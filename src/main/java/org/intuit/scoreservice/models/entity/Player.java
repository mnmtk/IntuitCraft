package org.intuit.scoreservice.models.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "players")
@Data // consider keeping only getters
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id; // Unique ID for Player

    @JsonProperty("username")
    private String username; // Player's username
}