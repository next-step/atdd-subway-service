package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

import static nextstep.subway.path.domain.DiscountAgeStrategy.*;

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
        this.fee = getAgeType(age).calculate(fee);
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
