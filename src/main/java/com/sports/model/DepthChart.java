package com.sports.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "depth_chart_position")
public class DepthChart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sports_depth_id")
    private int sportsDepthId;

    @Column(name = "position_name")
    private String positionName;

    @Column(name = "position_depth")
    private int positionDepth;

    @JsonManagedReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Override
    public String toString() {
        return "DepthChart[sportsDepthId=" + sportsDepthId + ", positionName=" + positionName
                + ", positionDepth=" + positionDepth+", Player="+(player!=null?player.toString():null)+"]";
    }
}
