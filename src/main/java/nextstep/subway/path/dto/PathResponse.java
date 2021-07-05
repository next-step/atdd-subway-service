package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.fare.RequireFare;
import nextstep.subway.path.domain.fare.discount.Discount;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse from(final Path shortestPath) {
        List<StationResponse> stationResponses = shortestPath.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(toList());

        return new PathResponse(stationResponses, shortestPath.getDistance(), RequireFare.getRequireFare(shortestPath));
    }

    public static PathResponse of(Discount discount, Path shortestPath) {
        List<StationResponse> stationResponses = shortestPath.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, shortestPath.getDistance(), RequireFare.getRequireFareWithDiscount(shortestPath, discount));
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
