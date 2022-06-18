package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.List;

public class Stations {
    private final List<Station> stationsList;

    public Stations() {
        this.stationsList = new ArrayList<>();
    }

    public Stations(List<Station> stationsList) {
        this.stationsList = stationsList;
    }

    public boolean isContainStations(Station station) {
        return stationsList.stream().anyMatch(it -> it == station);
    }

    public boolean isEmpty() {
        return stationsList.isEmpty();
    }

    public List<Station> getStationsList() {
        return stationsList;
    }
}
