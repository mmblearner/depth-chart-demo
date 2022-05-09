package com.sports.repository;

import com.sports.model.DepthChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerDepthRepository extends JpaRepository<DepthChart,Integer> {

    @Query("select max(positionDepth) from DepthChart dc where positionName = UPPER(?1)")
    Integer getMaxPositionDepth(String positionName);
    @Query("select dc from DepthChart dc where player.playerId = ?1")
    DepthChart findByPlayerId(int playerId);

    List<DepthChart> findAllByPositionNameAndPositionDepthOrderByPositionDepthAsc(String positionName, int positionDepth);

    List<DepthChart> findAllByPositionNameOrderByPositionDepthAsc(String positionName);

    List<DepthChart> findAllByOrderByPositionNameAscPositionDepthAsc();

    List<DepthChart> findAllByPositionNameAndPositionDepthGreaterThan(String positionName, int positionDepth);
}
