package nextstep.subway.station.domain;

import java.util.List;

public class Stations {
    private List<Station> stations;

    private Stations() {
    }

    private Stations(List<Station> stations) {
        this.stations = stations;
    }
    
    public static Stations from(List<Station> stations) {
        return new Stations(stations);
    }

    public List<Station> getStations() {
        return stations;
    }

    
    public boolean isInStations(List<Station> stations) {
        if (stations.stream().filter(station -> getStations().contains(station)).count() == stations.size()) {
            return true;
        }
        return false;
    }
}
