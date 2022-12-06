package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<PathStationResponse> stations;
    private int distance;
    private int price;

    public PathResponse() {}

    public PathResponse(List<PathStationResponse> stations, int distance, int price) {
        this.stations = stations;
        this.distance = distance;
        this.price = price;
    }

    public static PathResponse of(Path path) {
        return new PathResponse(path.getStations()
                .stream()
                .map(station -> PathStationResponse.of(station))
                .collect(Collectors.toList()), path.getDistance(), path.getPrice());
    }

    public List<PathStationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getPrice() {
        return price;
    }
}
