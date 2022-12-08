package nextstep.subway;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.member.domain.Member;
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

    public static Line createLine(String name, String color, Station upStation, Station downStation,
                                  int distance, int additionalFare) {
        return new Line(name, color, upStation, downStation, distance, additionalFare);
    }

    public static Favorite createFavorite(final Member member, final Station sourceStation, final Station targetStation,
                                          final Long id) {
        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        Field field = ReflectionUtils.findField(Favorite.class, "id");
        Objects.requireNonNull(field).setAccessible(true);
        ReflectionUtils.setField(field, favorite, id);
        return favorite;
    }

    public static Member createMember(final String email, final String password, final int age, final Long id) {
        Member member = new Member(email, password, age);
        Field field = ReflectionUtils.findField(Member.class, "id");
        Objects.requireNonNull(field).setAccessible(true);
        ReflectionUtils.setField(field, member, id);
        return member;
    }
}
