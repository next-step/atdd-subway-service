package nextstep.subway.station.domain;

import nextstep.subway.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Station extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    public Station() {
    }

    public Station(String name) {
        this.name = name;
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
        if (o == null || !(o instanceof Station)) return false;
        Station station = (Station) o;
        return Objects.equals(this.getId(), station.getId()) &&
                Objects.equals(this.getName(), station.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getName());
    }
}
