package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final List<Station> stations;
    private final Integer pathLength;
    private final Integer pathSurcharge;

    public Path(List<Station> stations, Integer pathLength, Integer pathSurcharge) {
        this.stations = stations;
        this.pathLength = pathLength;
        this.pathSurcharge = pathSurcharge;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Integer getPathLength() {
        return pathLength;
    }

    public Integer getPathSurcharge() {
        return pathSurcharge;
    }

}
