package com.sports.controller;

import com.sports.model.DepthChart;
import com.sports.payload.request.player.PlayerDepthChartRequest;
import com.sports.payload.response.player.PlayerDepthChartResponse;
import com.sports.payload.response.player.PlayerResponse;
import com.sports.service.PlayerDepthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "depthchart")
public class DepthChartController {

    @Autowired
    private PlayerDepthService playerDepthService;

    /**
     * Case1 : Add Player to Depth Chart
     * Assumption
     * 1) Player is created in system using player API
     * 2) player_id will consider as input instead of name as name can not be unique key
     * 3) using Put operation here as we are creating new depth position and probably will be creating new object if not exists.
     * (Upsert Operation)
     */

    @PutMapping(path = "/addPlayerDepth")
    public ResponseEntity<PlayerDepthChartResponse> addPlayerToDepthChart(@RequestBody PlayerDepthChartRequest playerDepthChartRequest) {
        log.info("Creating depth chart {}", playerDepthChartRequest);
        DepthChart depthChart = playerDepthService.addPlayerDepth(playerDepthChartRequest);
        return new ResponseEntity<PlayerDepthChartResponse>(
                PlayerDepthChartResponse.builder()
                        .playerDepthId(depthChart.getSportsDepthId())
                        .positionName(depthChart.getPositionName())
                        .positionDepth(depthChart.getPositionDepth())
                        .player(PlayerResponse.builder().playerId(depthChart.getPlayer().getPlayerId())
                                .name(depthChart.getPlayer().getName())
                                .sport(depthChart.getPlayer().getSport()).build())
                        .build(), HttpStatus.CREATED);
    }

    /**
     * @return
     */
    @GetMapping(path = "/getFullDepthChart", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List<DepthChart>> getFullDepthChart() {
        return playerDepthService.getFullDepthChartByPosition();
    }

    /**
     * @param positionName
     * @param playerId
     * @return
     */
    @GetMapping(path = "/getDepthChart/{positionName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DepthChart> getFullDepthChart(@PathVariable(name = "positionName") String positionName,
                                              @RequestParam(name = "playerId", required = false) Integer playerId) {
        log.info("Position Name {} , Player Id {}", positionName, playerId);
        return playerDepthService.getDepthChart(positionName, playerId);
    }
}
