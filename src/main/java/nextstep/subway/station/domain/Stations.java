package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Stations {

    private List<Station> stations = new ArrayList<>();

    public void add(Station downStation) {
        stations.add(downStation);
    }

    public void addAll(List<Station> stations) {
        this.stations.addAll(stations);
    }

    public Stream<Station> stream() {
        return stations.stream();
    }

    public boolean isEmpty() {
        return stations.isEmpty();
    }

    public boolean contains(Station station) {
        return stations.stream().anyMatch(s -> s.equals(station));
    }

    public List<Station> getStations() {
        return stations;
    }
}
