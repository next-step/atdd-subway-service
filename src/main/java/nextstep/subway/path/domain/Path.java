package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class Path {
    private List<Station> stations;
    private Long distance;

    public Path(List<Station> stations, Long distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public Long getDistance() {
        return distance;
    }
}
