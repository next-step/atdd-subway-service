package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Stations {
    List<Station> values = new ArrayList<>();

    public Stations() {

    }

    public Stations(List<Station> values) {
        this.values = values;
    }

    public void add(Station station) {
        this.add(station);
    }

    public void addAll(Stations stations) {
        this.values.addAll(stations.get());
    }

    public int size() {
        return this.values.size();
    }

    public List<Station> get() {
        return values;
    }

    public Station get(int index) {
        return values.get(index);
    }

    public int lastIndex() {
        return values.size() - 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stations stations = (Stations) o;
        return Objects.equals(values, stations.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}
