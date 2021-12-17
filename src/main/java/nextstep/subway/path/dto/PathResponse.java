package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;

    private int distance;

    private long fare;

    public PathResponse() {

    }

    public PathResponse(List<Station> stations, Distance distance, Fare fare) {
        this.stations = StationResponse.ofList(stations);
        this.distance = distance.getValue();
        this.fare = fare.getValueAsLong();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public long getFare() {
        return fare;
    }
}
