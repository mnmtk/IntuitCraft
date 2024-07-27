package org.intuit.scoreservice.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class RankResponse {
    @JsonProperty("username")
    private String username;
    @JsonProperty("score")
    private Long score;
}
