package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.station.domain.Station;

public class Stations {
    private final List<Station> stations;

    private Stations(List<Station> stations) {
        this.stations = stations;
    }

    public static Stations from(List<Station> stations) {
        return new Stations(stations);
    }

    public static Stations empty() {
        return new Stations(new ArrayList<>());
    }

    public void add(Station station) {
        this.stations.add(station);
    }

    public boolean contains(Station station) {
        return stations.contains(station);
    }

    public boolean isEmpty() {
        return stations.isEmpty();
    }

    public void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        stations.forEach(graph::addVertex);
    }

    public List<Station> getList() {
        return Collections.unmodifiableList(this.stations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stations stations1 = (Stations)o;
        return Objects.equals(stations, stations1.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations);
    }
}
