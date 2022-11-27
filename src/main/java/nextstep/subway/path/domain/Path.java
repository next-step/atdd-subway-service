package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

public class Path {
    private static final String CAN_NOT_EMPTY = "경로는 비어있을 수 없습니다.";

    private final List<Station> stations;
    private final Distance distance;

    public Path(List<Station> stations, int distance) {
        validate(stations);
        this.stations = new ArrayList<>(stations);
        this.distance = new Distance(distance);
    }

    private void validate(List<Station> stations) {
        if (stations == null || stations.isEmpty()) {
            throw new IllegalArgumentException(CAN_NOT_EMPTY);
        }
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistanceValue() {
        return distance.value();
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
        return Objects.equals(stations, path.stations) && Objects.equals(distance, path.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }
}
