package nextstep.subway.line.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nextstep.subway.station.domain.Station;

public class Path {
    private static final int MIN_DISTANCE = 1;
    private static final int MIN_STATION_SIZE = 2;
    private final List<Station> stations;
    private final Distance distance;

    private Path(List<Station> stations, Distance distance) {
        validateStations(stations);
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(List<Station> stations, int distance) {
        return new Path(stations, Distance.from(distance));
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    private void validateStations(List<Station> stations) {
        Set<Station> duplicatedStation = new HashSet<>(stations);

        if (duplicatedStation.size() != stations.size()) {
            throw new IllegalArgumentException("경로의 역은 중복될 수 없습니다.");
        }

        if (!stations.isEmpty() && stations.size() < MIN_STATION_SIZE) {
            throw new IllegalArgumentException("경로의 역은 2개 이상이어야 합니다.");
        }
    }
}
