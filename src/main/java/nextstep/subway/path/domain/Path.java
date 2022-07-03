package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final List<Station> stations;
    private final int distance;

    private final int fare;

    public static Path of(List<Station> stations, int distance, int additionalFare, int age) {
        return new Path(stations, distance, additionalFare, age);
    }

    public Path(List<Station> stations, int distance, int additionalFare, int age) {
        this.stations = stations;
        this.distance = distance;
        int totalFare = DistanceFarePolicy.BASIC_FARE + DistanceFarePolicy.calculateOverFare(distance) + additionalFare;
        this.fare = totalFare - FareSalePolicy.calculateSaleByAge(age, totalFare);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
