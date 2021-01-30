package nextstep.subway.line.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

public class Stations {

    private List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

    public boolean anyMatch(Station station) {
        return stations.stream().anyMatch(it -> it == station);
    }
    public boolean isEmpty() {
        return stations.isEmpty();
    }

    public boolean noneMatch(Station station) {
        return stations.stream().noneMatch(it -> it == station);
    }
}
