package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.path.domain.SectionWeightedEdge;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationSimpleResponse;
import org.jgrapht.GraphPath;

public class ShortestPathResponse {
    private List<StationSimpleResponse> stations;
    private int distance;

    public ShortestPathResponse() {

    }

    private ShortestPathResponse(int distance, List<StationSimpleResponse> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public static ShortestPathResponse of(
        GraphPath<Station, SectionWeightedEdge> path,
        List<StationSimpleResponse> stationResponses
    ) {
        return new ShortestPathResponse((int) path.getWeight(), stationResponses);
    }

    public int getDistance() {
        return distance;
    }

    public List<StationSimpleResponse> getStations() {
        return stations;
    }
}
