package nextstep.subway.station.domain;

import io.jsonwebtoken.lang.Assert;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Stations {

    private static final Stations EMPTY = new Stations(Collections.emptyList());

    private final List<Station> list;

    private Stations(List<Station> list) {
        Assert.notNull(list, "지하철 역 목록은 필수입니다.");
        this.list = list;
    }

    public static Stations from(List<Station> list) {
        return new Stations(list);
    }

    public static Stations empty() {
        return EMPTY;
    }

    public boolean sizeLessThan(int target) {
        return list.size() < target;
    }

    public List<Station> list() {
        return Collections.unmodifiableList(list);
    }

    public Stations merge(Stations stations) {
        List<Station> newList = new ArrayList<>(list);
        newList.addAll(stations.list);
        return from(newList);
    }

    public <R> List<R> mapToList(Function<Station, R> mapper) {
        return list.stream()
            .map(mapper)
            .collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
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
        return Objects.equals(list, stations.list);
    }

    @Override
    public String toString() {
        return "Stations{" +
            "list=" + list +
            '}';
    }
}
