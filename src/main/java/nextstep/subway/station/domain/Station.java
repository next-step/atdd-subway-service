package nextstep.subway.station.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.common.BaseEntity;
import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true, of = "name")
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_station_name", columnNames={"name"}))
public class Station extends BaseEntity {

    private String name;

    public Station(String name) {
        this.name = name;
    }

    public Station(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s", name);
    }
}
