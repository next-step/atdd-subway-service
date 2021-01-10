package nextstep.subway.path.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    public static PathResponse of(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        List<StationResponse> stationResponses = shortestPath.getVertexList()
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        return new PathResponse(stationResponses, (int) shortestPath.getWeight());
    }
}
