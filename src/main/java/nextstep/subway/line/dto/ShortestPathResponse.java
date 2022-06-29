package nextstep.subway.line.dto;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class ShortestPathResponse {
    private int distance;
    private List<Station> stations;

    public ShortestPathResponse() {

    }

    public ShortestPathResponse(int distance, List<Station> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public static ShortestPathResponse from(GraphPath path) {
        return new ShortestPathResponse((int) path.getWeight(), path.getVertexList());
    }

    public int getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }
}
