package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

public class Path {

    private final List<Station> stations;
    private final int distance;
    private int fee;

    private Path(List<Station> stations, int distance, int fee) {
        this.stations = stations;
        this.distance = distance;
        this.fee = fee;
    }

    public static Path of(List<Station> stations, int distance, int fee) {
        return new Path(stations, distance, fee);
    }

    public List<Station> stations() {
        return stations;
    }

    public int distance() {
        return distance;
    }

    public int fee() {
        return fee;
    }

    public void applyDiscountAgeStrategy(int age) {
        if (isTeenagerAge(age)){
            this.fee = (int) ((fee - 350) * 0.8);
        }

        if (isChildAge(age)){
            this.fee = (int) ((fee - 350) * 0.5);
        }
    }

    private boolean isChildAge(int age) {
        return age >= 6 && age < 12;
    }

    private boolean isTeenagerAge(int age) {
        return age >= 13 && age < 19;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return distance == path.distance && Objects.equals(stations, path.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }

    @Override
    public String toString() {
        return "Path{" +
                "stations=" + stations +
                ", distance=" + distance +
                '}';
    }
}
