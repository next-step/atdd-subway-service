package nextstep.subway.path.domain;

import java.util.Objects;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Stations;
import org.springframework.util.Assert;

public final class Path {

    private static final int MINIMUM_STATIONS_SIZE = 2;

    private final Stations stations;
    private final Distance distance;
    private final Sections sections;

    private Path(Stations stations, Distance distance, Sections sections) {
        Assert.notNull(stations, "지하철 역 경로는 필수입니다.");
        Assert.notNull(distance, "거리는 필수입니다.");
        Assert.isTrue(isNotEmpty(sections), "지하철 구간들은 필수입니다.");
        validateSize(stations);
        this.stations = stations;
        this.distance = distance;
        this.sections = sections;
    }

    public static Path of(Stations stations, Distance distance, Sections sections) {
        return new Path(stations, distance, sections);
    }

    public Stations stations() {
        return stations;
    }

    public Distance distance() {
        return distance;
    }

    public Sections sections() {
        return sections;
    }

    private boolean isNotEmpty(Sections sections) {
        return sections != null && sections.isNotEmpty();
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
