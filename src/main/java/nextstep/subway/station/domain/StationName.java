package nextstep.subway.station.domain;

import nextstep.subway.exception.EmptyStationNameException;
import nextstep.subway.message.ExceptionMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class StationName {
    @Column(unique = true, nullable = false)
    private String name;

    protected StationName() {
    }

    private StationName(String name) {
        this.name = name;
    }

    public static StationName from(String name) {
        checkNotNull(name);
        return new StationName(name);
    }

    private static void checkNotNull(String name) {
        if (name == null || name.isEmpty()) {
            throw new EmptyStationNameException(ExceptionMessage.EMPTY_STATION_NAME);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationName that = (StationName) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
