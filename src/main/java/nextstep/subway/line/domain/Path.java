package nextstep.subway.line.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nextstep.subway.station.domain.Station;

public class Path {
    private static final int MIN_DISTANCE = 1;
    private static final int MIN_STATION_SIZE = 2;
    private final List<Station> stations;
    private final int distance;

    private Path(List<Station> stations, int distance) {
        validateDistance(distance);
        validateStations(stations);
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(List<Station> stations, int distance) {
        return new Path(stations, distance);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    private void validateDistance(int distance) {
        if (distance < MIN_DISTANCE) {
            throw new IllegalArgumentException("경로의 거리는 1 이상이어야 합니다.");
        }

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
