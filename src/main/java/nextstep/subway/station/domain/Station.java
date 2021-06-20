package nextstep.subway.station.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.BaseEntity;
import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
public class Station extends BaseEntity {
    @Column(unique = true)
    private String name;

    public Station(String name) {
        this.name = name;
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
