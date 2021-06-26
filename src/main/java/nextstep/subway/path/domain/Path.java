package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {

    private final List<Station> stations;
    private final int distance;

    public Path(List<Station> pathStations, int totalDistance) {
        this.stations = pathStations;
        this.distance = totalDistance;
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }
}
