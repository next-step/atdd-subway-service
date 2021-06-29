package nextstep.subway.path.dto;

import nextstep.subway.line.dto.StationResponses;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;

import java.util.List;

public class LinePathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public LinePathResponse() {
    }

    public LinePathResponse(Stations stations, Distance distance, Money fare) {
        this.distance = distance.toInt();
        this.stations = new StationResponses(stations).toCollection();
        this.fare = fare.toInt();
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
