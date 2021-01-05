package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.Objects;

public class PathStation {
    private Long id;
    private String name;

    public PathStation(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static PathStation of(Station station) {
        return new PathStation(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathStation that = (PathStation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
