package nextstep.subway.station.domain;


import nextstep.subway.line.domain.Section;

import java.util.ArrayList;
import java.util.List;

public class Stations {

    private List<Station> stations = new ArrayList<>();

    public Stations() {
    }

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public boolean isNoneMatchStation(Section section) {
        return stations.stream().noneMatch(section::isSameAnyStation);
    }

    public boolean isAnyMatchUpStation(Section section) {
        return stations.stream().anyMatch(section::isSameUpStation);
    }

    public boolean isAnyMatchDownStation(Section section) {
        return stations.stream().anyMatch(section::isSameDownStation);
    }

    public void add(Station station) {
        stations.add(station);
    }

    public boolean contains(Station station) {
        return stations.contains(station);
    }

    public List<Station> getStations() {
        return stations;
    }
}
