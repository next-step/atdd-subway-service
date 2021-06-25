package nextstep.subway.path.dto;

import java.util.List;
import java.util.Objects;
import nextstep.subway.path.domain.DiscountStrategy;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.dto.StationResponse;

import static java.util.stream.Collectors.toList;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse() {

    }

    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(ShortestPath shortestPath, DiscountStrategy discountStrategy) {

        List<StationResponse> stationResponses = shortestPath.getPath()
                                                             .stream()
                                                             .map(StationResponse::of)
                                                             .collect(toList());

        Fare fare = shortestPath.getSubwayFare();

        return new PathResponse(stationResponses, shortestPath.getDistance().getValue(), fare.getTotalFare(discountStrategy));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PathResponse that = (PathResponse) o;
        return distance == that.distance && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }
}
