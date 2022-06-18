package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Stations {
    private List<Station> stations = new ArrayList<>();

    public Stations() {
    }

    private Stations(List<Station> stations) {
        this.stations = stations;
    }

    public void add(Station station) {
        stations.add(station);
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Station> distinctStations() {
        return stations.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public boolean isNotEmpty() {
        return !stations.isEmpty();
    }

    public static Stations of(List<Station> stations) {
        return new Stations(stations);
    }
}
