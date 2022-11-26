package nextstep.subway;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;

public class Fixture {

    public static Section createSection(Station upStation, Station downStation, int distance) {
        return new Section(upStation, downStation, distance);
    }

    public static Station createStation(final String name, final long id) {
        Station station = new Station(name);
        Field field = ReflectionUtils.findField(Station.class, "id");
        Objects.requireNonNull(field).setAccessible(true);
        ReflectionUtils.setField(field, station, id);
        return station;
    }

    public static Line createLine(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }
}
