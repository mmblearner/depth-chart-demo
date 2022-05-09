package com.sports.service.impl;

import com.sports.exception.DataNotFoundException;
import com.sports.exception.InvalidServiceRequestException;
import com.sports.model.DepthChart;
import com.sports.model.Player;
import com.sports.payload.request.player.PlayerDepthChartRequest;
import com.sports.payload.response.Error;
import com.sports.repository.PlayerDepthRepository;
import com.sports.repository.PlayerRepository;
import com.sports.service.PlayerDepthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@Service
public class PlayerDepthServiceImpl implements PlayerDepthService {
    @Autowired
    private PlayerDepthRepository playerDepthRepository;

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * @param playerDepthChartRequest
     * @return
     */
    @Override
    public DepthChart addPlayerDepth(PlayerDepthChartRequest playerDepthChartRequest) {
        DepthChart finalDepthChartToUpdate = null;

        // 1. Check If player exists. If not then operation not allowed ask to create the Player object first.
        Optional<Player> player = playerRepository.findById(playerDepthChartRequest.getPlayerId());
        if (!player.isPresent()) {
            throw new DataNotFoundException(Error.builder().errorCode("EX-1003").summary("Player Not Found").description("Player Not Found").build());
        }

        DepthChart existingDepthChart = getExistingDepthChartByPlayerId(playerDepthChartRequest);

        int maxDepthForPosition = 0;
        List<DepthChart> depthChartsByPosition = playerDepthRepository.findAllByPositionNameOrderByPositionDepthAsc(playerDepthChartRequest.getPositionName());
        if (!CollectionUtils.isEmpty(depthChartsByPosition)) {
            maxDepthForPosition = depthChartsByPosition.get(depthChartsByPosition.size() - 1).getPositionDepth();
        }

        // 2. Check If position depth is present in request or not and accordingly create object
        if (playerDepthChartRequest.getPositionDepth() <= 0) {
            finalDepthChartToUpdate = createDepthChartIfPositionNotGiven(playerDepthChartRequest, existingDepthChart, player, maxDepthForPosition);
        } else {
            finalDepthChartToUpdate = createDepthChartWithPosition(playerDepthChartRequest, existingDepthChart, player, maxDepthForPosition);
        }
        return playerDepthRepository.save(finalDepthChartToUpdate);
    }

    @Override
    public Map<String, List<DepthChart>> getFullDepthChartByPosition() {
        List<DepthChart> depthCharts = playerDepthRepository.findAllByOrderByPositionNameAscPositionDepthAsc();
        log.info("depthCharts  " + depthCharts);
//        Map<String, List<DepthChart>> depthChartsMap =
//                depthCharts.stream().collect(Collectors.groupingBy(DepthChart::getPositionName, Collectors.toList()));

        Map<String, List<DepthChart>> depthChartsMap = depthCharts.stream().collect(
                Collectors.groupingBy(DepthChart::getPositionName, HashMap::new, Collectors.toCollection(ArrayList::new))
        );
        log.info("depthChartsMap  " + depthChartsMap);
        return depthChartsMap;
    }

    @Override
    public List<DepthChart> getDepthChart(String positionName, Integer playerId) {
        if (StringUtils.isEmpty(positionName)) {
            throw new InvalidServiceRequestException(Error.builder().errorCode("EX-1003").summary("Invalid Position Provided").description("Please provide correct position name.").build());
        }

        if (!StringUtils.isEmpty(positionName) && playerId != null) {
            Player player = playerRepository.getById(playerId);
            if (null == player) {
                throw new DataNotFoundException(Error.builder().errorCode("EX-1003").summary("Invalid Player Id").description("Invalid Player Id in request. Can not find players under given player id.").build());
            }
            DepthChart depthChart = playerDepthRepository.findByPlayerId(playerId);
            if (null == depthChart) {
                return Collections.<DepthChart>emptyList();
            } else {
                return playerDepthRepository.findAllByPositionNameAndPositionDepthGreaterThan(positionName, depthChart.getPositionDepth());
            }
        } else {
            return playerDepthRepository.findAllByPositionNameOrderByPositionDepthAsc(positionName);
        }
    }

    /**
     * @param playerDepthChartRequest
     * @param existingDepthChart
     * @param player
     * @param maxDepthForPosition
     * @return
     */
    private DepthChart createDepthChartWithPosition(PlayerDepthChartRequest playerDepthChartRequest, DepthChart existingDepthChart, Optional<Player> player, int maxDepthForPosition) {
        DepthChart finalDepthChartToUpdate = null;
        log.info("Existing Depth Chart {} ", existingDepthChart);

        // Check if any existing player for same position and depth
        List<DepthChart> existingDepthChartByPosition = playerDepthRepository.findAllByPositionNameAndPositionDepthOrderByPositionDepthAsc(playerDepthChartRequest.getPositionName(), playerDepthChartRequest.getPositionDepth());
        log.info("Existing Depth Chart by position " + existingDepthChartByPosition);
        if (null != existingDepthChart) {
            int existingPlayerOlderPosition = 0;
            if (!CollectionUtils.isEmpty(existingDepthChartByPosition)) {
                existingPlayerOlderPosition = updateExistingPlayerAtSamePositionToLowestPriority(existingDepthChart, maxDepthForPosition, existingDepthChartByPosition);
                existingDepthChart.setPositionDepth(existingPlayerOlderPosition);
            } else {
                existingDepthChart.setPositionDepth(playerDepthChartRequest.getPositionDepth());
            }
            existingDepthChart.setPositionName(playerDepthChartRequest.getPositionName());
            return existingDepthChart;
        } else {
            DepthChart depthChart = DepthChart.builder().positionName(playerDepthChartRequest.getPositionName())
                    .positionDepth(playerDepthChartRequest.getPositionDepth()).player(player.get()).build();
            if (!CollectionUtils.isEmpty(existingDepthChartByPosition)) {
                // Get Last Element in List which is giving sorted ASC result
                int playerOlderPositionDepth = updateExistingPlayerAtSamePositionToLowestPriority(existingDepthChart, maxDepthForPosition, existingDepthChartByPosition);
                depthChart.setPositionDepth(playerOlderPositionDepth);
            }
            return depthChart;
        }
    }

    private int updateExistingPlayerAtSamePositionToLowestPriority(DepthChart existingDepthChart, int maxDepthForPosition, List<DepthChart> existingDepthChartByPosition) {

        // Get Last Element in List which is giving sorted ASC result
        DepthChart existingFirstPriorityPlayer = existingDepthChartByPosition.get(0);
        int existingPLayerDepth = existingFirstPriorityPlayer.getPositionDepth();
        // Set Existing First player at same position to Last Position
        existingFirstPriorityPlayer.setPositionDepth(maxDepthForPosition);
        // Update older Player at same position to lowest priority
        playerDepthRepository.save(existingFirstPriorityPlayer);
        return existingPLayerDepth;
    }

    private DepthChart createDepthChartIfPositionNotGiven(PlayerDepthChartRequest playerDepthChartRequest, DepthChart existingDepthChart, Optional<Player> player, int maxDepthForPosition) {
        existingDepthChart = getExistingDepthChartByPlayerId(playerDepthChartRequest);
        log.info("Max depth for position {}", maxDepthForPosition);
        if (null != existingDepthChart) {
            existingDepthChart.setPositionName(playerDepthChartRequest.getPositionName());
            existingDepthChart.setPositionDepth(maxDepthForPosition + 1);
            return existingDepthChart;
        } else {
            return DepthChart.builder().positionName(playerDepthChartRequest.getPositionName())
                    .positionDepth(maxDepthForPosition + 1).player(player.get()).build();
        }
    }

    private DepthChart getExistingDepthChartByPlayerId(PlayerDepthChartRequest playerDepthChartRequest) {
        DepthChart existingDepthChart = playerDepthRepository.findByPlayerId(playerDepthChartRequest.getPlayerId());
        return existingDepthChart;
    }
}
