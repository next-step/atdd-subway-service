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

    protected Station() {
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

    public static Station of(Long id, String name) {
        return new Station(id, name);
    }

    public Long getId() {
        return id;
    }

    public StationName getName() {
        return name;
    }

    public String getNameValue() {
        return Objects.isNull(this.getName()) ? "" : this.getName().getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equals(getId(), station.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
