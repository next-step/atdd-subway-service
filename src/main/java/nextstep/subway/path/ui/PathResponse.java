package nextstep.subway.path.ui;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse(final List<StationResponse> stations, final Distance distance, final Fare fare) {
        this.stations = stations;
        this.distance = distance.value();
        this.fare = fare.value();
    }

    protected PathResponse() {
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
