package nextstep.subway.station.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.util.Assert;

public final class Stations {

    private static final Stations EMPTY = new Stations(Collections.emptyList());

    private final List<Station> stations;

    private Stations(List<Station> stations) {
        Assert.notNull(stations, "지하철 역 목록은 필수입니다.");
        this.stations = stations;
    }

    public static Stations from(List<Station> stations) {
        return new Stations(stations);
    }

    public static Stations empty() {
        return EMPTY;
    }

    public boolean sizeLessThan(int target) {
        return stations.size() < target;
    }

    public List<Station> list() {
        return Collections.unmodifiableList(stations);
    }

    public Stations merge(Stations stations) {
        List<Station> newList = new ArrayList<>(this.stations);
        newList.addAll(stations.stations);
        return from(newList);
    }

    public <R> List<R> mapToList(Function<Station, R> mapper) {
        return stations.stream()
            .map(mapper)
            .collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stations stations = (Stations) o;
        return Objects.equals(this.stations, stations.stations);
    }

    @Override
    public String toString() {
        return "Stations{" +
            "stations=" + stations +
            '}';
    }
}
