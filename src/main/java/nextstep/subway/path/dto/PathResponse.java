package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;
    private double distance;
    private double price;

    public PathResponse(List<StationResponse> stations, double distance, double price) {
        this.stations = stations;
        this.distance = distance;
        this.price = price;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }

    public double getPrice() {
        return price;
    }
}