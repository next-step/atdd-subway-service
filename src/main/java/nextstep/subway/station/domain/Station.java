package nextstep.subway.station.domain;

import java.security.InvalidParameterException;
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

    protected Station() {
    }

    public Station(String name) {
        validEmpty(name);

        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private void validEmpty(String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new InvalidParameterException("빈 값을 입력 할 수 없습니다.");
        }
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

        if (Objects.isNull(id)
            && Objects.isNull(station.getId()
        )) {
            return Objects.equals(name, station.name);
        }

        return Objects.equals(id, station.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
