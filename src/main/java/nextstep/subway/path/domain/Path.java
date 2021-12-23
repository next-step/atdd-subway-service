package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class Path {
    public static final int MIN_SIZE_OF_STATION = 2;
    private final Distance distance;
    private List<Station> stations = new ArrayList<>();

    public Path(List<Station> stations, Distance distance) {
        validateStations(stations);
        this.distance = distance;
        this.stations = stations;
    }

    private void validateStations(List<Station> stations) {
        if (stations.size() < MIN_SIZE_OF_STATION) {
            throw new IllegalArgumentException("경로의 station 갯수는 " + MIN_SIZE_OF_STATION + "개 이상이어야 합니다.");
        }
    }

    public Distance getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }
}
