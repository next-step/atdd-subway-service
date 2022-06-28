package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    private PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(Path shortestPath, int fare) {
        List<StationResponse> stationResponses = new ArrayList<>();
        for(Station station : shortestPath.getStations()) {
            stationResponses.add(StationResponse.of(station));
        }
        return new PathResponse(stationResponses, shortestPath.getDistance(), fare);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
