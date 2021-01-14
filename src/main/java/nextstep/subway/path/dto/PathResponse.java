package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int payment;

    private PathResponse(List<StationResponse> stations, int distance, int payment) {
        this.stations = stations;
        this.distance = distance;
        this.payment = payment;
    }

    public static PathResponse create(List<Station> stations, int distance, int payment) {
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(stationResponses, distance, payment);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getPayment() {
        return payment;
    }
}
