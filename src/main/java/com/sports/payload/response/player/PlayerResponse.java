package com.sports.payload.response.player;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class PlayerResponse {
    private int playerId;
    private String name;
    private String sport;
}
