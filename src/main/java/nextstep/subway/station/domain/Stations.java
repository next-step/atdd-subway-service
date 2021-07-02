package nextstep.subway.station.domain;

import java.util.List;

public class Stations {
    List<Station> values;

    private Stations() {

    }

    public Stations(List<Station> values) {
        this.values = values;
    }

    public void add(Station station) {
        this.add(station);
    }

    public int size() {
        return this.values.size();
    }

    public List<Station> get() {
        return values;
    }
}
