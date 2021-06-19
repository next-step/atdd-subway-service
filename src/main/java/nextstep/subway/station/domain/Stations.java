package nextstep.subway.station.domain;

import java.util.List;

public class Stations {

    private List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public boolean hasStation(Station station) {
        return stations.contains(station);
    }

    public List<Station> stations() {
        return stations;
    }
}
