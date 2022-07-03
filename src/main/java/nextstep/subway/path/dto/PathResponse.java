package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.fare.DistanceFareCalculationPolicy;
import nextstep.subway.path.domain.fare.FareCalculationPolicy;
import nextstep.subway.path.domain.fare.discount.DiscountPolicy;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.path.domain.fare.discount.AgeDiscountPolicy.ADULT;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    private PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse from(Path shortestPath) {
        int distance = shortestPath.getDistance();
        FareCalculationPolicy distanceFareCalculator = new DistanceFareCalculationPolicy(ADULT, distance);
        int totalFare = distanceFareCalculator.calculateFare() + shortestPath.getAdditionalFare();

        return new PathResponse(getStationResponses(shortestPath), distance, totalFare);
    }

    public static PathResponse of(DiscountPolicy discountPolicy, Path shortestPath) {
        int distance = shortestPath.getDistance();
        FareCalculationPolicy distanceFareCalculator = new DistanceFareCalculationPolicy(discountPolicy, distance);
        int totalFare = distanceFareCalculator.calculateFare() + shortestPath.getAdditionalFare();

        return new PathResponse(getStationResponses(shortestPath), distance, totalFare);
    }

    private static List<StationResponse> getStationResponses(Path shortestPath) {
        return shortestPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
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
