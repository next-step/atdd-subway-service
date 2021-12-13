package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;

    private int distance;

    private int fare;

    public PathResponse() {

    }

    public PathResponse(List<Station> stations, int distance, int fare) {
        this.stations = StationResponse.ofList(stations);
        this.distance = distance;
        this.fare = fare;
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
