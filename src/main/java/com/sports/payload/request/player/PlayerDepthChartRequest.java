package com.sports.payload.request.player;

import com.sports.model.Player;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDepthChartRequest {
    private int playerId;
    private String positionName;
    private int positionDepth;
}
