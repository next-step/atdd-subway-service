package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    final private int distance;
    final private List<StationResponse> stations;

    private PathResponse(int distance, List<StationResponse> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public static PathResponse of(int distance, List<Station> stations) {
        return new PathResponse(distance, StationResponse.ofList(stations));
    }

    public static PathResponse of(Path path) {
        return new PathResponse(path.getTotalDistance(), StationResponse.ofList(path.getStations()));
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
