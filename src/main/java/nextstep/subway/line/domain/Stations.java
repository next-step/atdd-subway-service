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

}
