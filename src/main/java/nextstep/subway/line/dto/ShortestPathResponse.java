package nextstep.subway.line.dto;

import java.util.List;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;

public class ShortestPathResponse {
    private int distance;
    private List<StationResponse> stations;

    public ShortestPathResponse() {

    }

    private ShortestPathResponse(int distance, List<StationResponse> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public static ShortestPathResponse of(GraphPath path, List<StationResponse> stationResponses) {
        return new ShortestPathResponse((int) path.getWeight(), stationResponses);
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
