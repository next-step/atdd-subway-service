package nextstep.subway.path.domain;

import io.jsonwebtoken.lang.Assert;
import java.util.Objects;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Stations;

public final class Path {

    private static final int MINIMUM_STATIONS_SIZE = 2;

    private final Stations stations;
    private final Distance distance;

    private Path(Stations stations, Distance distance) {
        Assert.notNull(stations, "지하철 역 경로는 필수입니다.");
        Assert.notNull(distance, "거리는 필수입니다.");
        validateSize(stations);
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(Stations stations, Distance distance) {
        return new Path(stations, distance);
    }

    public Stations stations() {
        return stations;
    }

    public Distance distance() {
        return distance;
    }

    private void validateSize(Stations stations) {
        if (stations.sizeLessThan(MINIMUM_STATIONS_SIZE)) {
            throw new IllegalArgumentException(
                String.format("경로의 지하철 역들은 적어도 %d개 이상 존재해야 합니다.", MINIMUM_STATIONS_SIZE));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path path = (Path) o;
        return Objects.equals(stations, path.stations);
    }

    @Override
    public String toString() {
        return "Path{" +
            "stations=" + stations +
            ", distance=" + distance +
            '}';
    }
}
