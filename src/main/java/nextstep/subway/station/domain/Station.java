package nextstep.subway.station.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.BaseEntity;
import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(of = {"id", "name"})
public class Station extends BaseEntity {
    @Column(unique = true)
    private String name;

    public Station(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s", name);
    }
}
