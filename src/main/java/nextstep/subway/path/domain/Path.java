package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class Path {
    public static final int MIN_SIZE_OF_STATION = 2;
    private final Distance distance;
    private List<Station> stations = new ArrayList<>();
    private final Sections sections;

    public Path(List<Station> stations, Distance distance) {
        this(stations, distance, new Sections());
    }

    public Path(List<Station> stations, Distance distance, Sections sections) {
        validateStations(stations);
        this.stations = stations;
        this.distance = distance;
        this.sections = sections;
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

    public int getMaxOverFareOfLine() {
        return this.sections.getMaxOverFareOfLine();
    }
}
