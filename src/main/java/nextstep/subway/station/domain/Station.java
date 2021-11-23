package nextstep.subway.station.domain;

import nextstep.subway.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Station extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private StationName name;

    protected Station() {}

    private Station(Long id) {
        this.id = id;
    }

    private Station(String name) {
        this.name = StationName.from(name);
    }

    private Station(Long id, String name) {
        this.id = id;
        this.name = StationName.from(name);
    }

    public static Station from(String name) {
        return new Station(name);
    }

    public static Station from(Long stationId) {
        return new Station(stationId);
    }

    public static Station of(Long id, String name) {
        return new Station(id, name);
    }

    public Long getId() {
        return id;
    }

    public StationName getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(id, station.id) &&
                Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
