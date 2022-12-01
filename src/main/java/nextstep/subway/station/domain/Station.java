package nextstep.subway.station.domain;

import nextstep.subway.BaseEntity;

import javax.persistence.*;
import java.util.Objects;
import nextstep.subway.common.exception.InvalidParameterException;
import org.apache.commons.lang3.StringUtils;

@Entity
public class Station extends BaseEntity {
    private static final String ERROR_MESSAGE_IS_BLANK_NAME = "지하철명은 필수 입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    protected Station() {
    }

    private Station(String name) {
        this.name = name;
    }

    public static Station from(String name) {
        validName(name);

        return new Station(name);
    }

    private static void validName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new InvalidParameterException(ERROR_MESSAGE_IS_BLANK_NAME);
        }
    }

    public boolean isSameStation(Station compareStation) {
        return Objects.equals(this, compareStation);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
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
        return Objects.equals(id, station.id)
                && Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
