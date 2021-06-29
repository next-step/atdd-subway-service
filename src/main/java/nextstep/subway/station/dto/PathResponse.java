package nextstep.subway.station.dto;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    public static PathResponse of(GraphPath graphPath) {
        List<Station> paths = graphPath.getVertexList();
        List<StationResponse> stations = StationResponse.asList(paths);
        return new PathResponse(stations, (int)graphPath.getWeight());
    }

    private PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public PathResponse() {
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
