package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

public class Path {

    private List<Station> stations;
    private int distance;
    private int extraFare;
    private int fare;

    private Path(List<Station> stations, int distance, int extraFare) {
        this.stations = stations;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public void calculateFare(int age) {
        this.fare = FareCalculator.calculate(distance, age, extraFare);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getExtraFare() {
        return extraFare;
    }

    public int getFare() {
        return fare;
    }

    public static class Builder {

        private List<Station> stations;
        private int distance;
        private int extraFare;

        public Builder stations(List<Station> stations) {
            this.stations = stations;
            return this;
        }

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public Builder extraFare(int extraFare) {
            this.extraFare = extraFare;
            return this;
        }

        public Path build() {
            return new Path(stations, distance, extraFare);
        }
    }
}
