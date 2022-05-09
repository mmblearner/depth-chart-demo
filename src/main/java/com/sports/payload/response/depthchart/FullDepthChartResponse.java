package com.sports.payload.response.depthchart;


import com.sports.model.DepthChart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FullDepthChartResponse {
    private Map<String, List<DepthChart>> fullDepthChart;
}
