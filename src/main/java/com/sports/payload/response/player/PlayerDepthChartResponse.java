package com.sports.payload.response.player;

import com.sports.model.Player;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PlayerDepthChartResponse {
    private int playerDepthId;
    private String positionName;
    private int positionDepth;
    private PlayerResponse player;

}
