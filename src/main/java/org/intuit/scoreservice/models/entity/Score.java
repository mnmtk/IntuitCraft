package org.intuit.scoreservice.models.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // consider keeping only setters to avoid data editing
@NoArgsConstructor
@AllArgsConstructor
public class Score {
    @JsonProperty("playerId")
    private Long playerId;

    @JsonProperty("score")
    private Long score;
}