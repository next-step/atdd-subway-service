package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class Path {

    private final List<Station> stations;
    private final int distance;
    private final int lineFare;

    public Path(List<Station> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.lineFare = fare;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getLineFare() {
        return lineFare;
    }
}
