package com.sports.service;

import com.sports.model.DepthChart;
import com.sports.payload.request.player.PlayerDepthChartRequest;

import java.util.List;
import java.util.Map;

public interface PlayerDepthService {
    DepthChart addPlayerDepth(PlayerDepthChartRequest playerDepthChartRequest);

    Map<String, List<DepthChart>> getFullDepthChartByPosition();

    List<DepthChart> getDepthChart(String positionName, Integer playerId);
}
