package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {

    private final List<Station> pathStations;
    private final int totalDistance;

    public Path(List<Station> pathStations, int totalDistance) {
        this.pathStations = pathStations;
        this.totalDistance = totalDistance;
    }

    public List<Station> getPathStations() {
        return this.pathStations;
    }

    public int getTotalDistance() {
        return this.totalDistance;
    }
}
