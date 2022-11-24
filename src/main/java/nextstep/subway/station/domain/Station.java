package nextstep.subway.station.domain;

import nextstep.subway.BaseEntity;

import javax.persistence.*;
import java.util.Objects;
import nextstep.subway.line.domain.Name;

@Entity
public class Station extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;

    public Station() {}

    public Station(String name) {
        this.name = Name.from(name);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
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
        Station station = (Station) o;
        return Objects.equals(getId(), station.getId()) && Objects.equals(getName(), station.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
