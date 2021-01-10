package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Stations implements Iterable<Station> {
    public static final Stations EMPTY_STATIONS = new Stations();

    private final List<Station> stations;

    public Stations() {
        stations = new ArrayList<>();
    }

    public void add(Station station) {
        stations.add(station);
    }

    public Stream<Station> stream() {
        return stations.stream();
    }

    public static Stations emptyStations() {
        return EMPTY_STATIONS;
    }

    public boolean contains(Station station) {
        return stations.contains(station);
    }

    public boolean isEmpty() {
        return stations.isEmpty();
    }

    @Override
    public Iterator<Station> iterator() {
        return stations.iterator();
    }

    @Override
    public void forEach(Consumer<? super Station> action) {
        stations.forEach(action);
    }
}
