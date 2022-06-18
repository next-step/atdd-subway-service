package nextstep.subway.path.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

public class Path {
    private static final int MIN_STATION_SIZE = 2;
    private final List<Station> stations;
    private final Distance distance;
    private final Fare fare;

    private Path(List<Station> stations, Distance distance, Fare fare) {
        validateStations(stations);
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static Path of(List<Station> stations, Distance distance, Fare fare) {
        return new Path(stations, distance, fare);
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public Fare getFare() {
        return fare;
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
