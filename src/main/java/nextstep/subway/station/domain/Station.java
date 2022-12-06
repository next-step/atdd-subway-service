package nextstep.subway.station.domain;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.common.domain.BaseEntity;
import nextstep.subway.common.domain.Name;

@Entity
public class Station extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    protected Station() {
    }

    private Station(Name name) {
        this.name = name;
    }

    private Station(Long id, Name name) {
        this.id = id;
        this.name = name;
    }

    public static Station from(Name name) {
        return new Station(name);
    }

    public static Station of(Long id, Name name) {
        return new Station(id, name);
    }

    public Long getId() {
        return id;
    }
    public Name name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station)o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
