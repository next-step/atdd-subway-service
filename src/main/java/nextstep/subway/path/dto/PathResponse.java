package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private Integer distance;
    private Integer fare;

    public PathResponse() {
    }

    public PathResponse(List<Station> stations, int distance, Fare fare) {
        this.stations = StationResponse.ofList(stations);
        this.distance = distance;
        this.fare = fare.getAmount();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getFare() {
        return fare;
    }
}
