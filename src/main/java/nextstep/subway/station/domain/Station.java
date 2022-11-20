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

    public Station() {
    }

    public Station(String name) {
        this.name = Name.from(name);
    }

    public boolean isSameStation(Station station) {
        return Objects.equals(this.name, station.name);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }
}
