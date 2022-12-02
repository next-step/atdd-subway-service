package nextstep.subway;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;

public class Fixture {
    public static Station createStation(String name, long id) {
        Station station = new Station(name);
        Field field = ReflectionUtils.findField(Station.class, "id");
        Objects.requireNonNull(field).setAccessible(true);
        ReflectionUtils.setField(field, station, id);
        return station;
    }

    public static Line createLine(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }

    public static Member createMember(long id, String email, String password, Integer age) {
        Member member = new Member(email, password, age);
        Field field = ReflectionUtils.findField(Member.class, "id");
        Objects.requireNonNull(field).setAccessible(true);
        ReflectionUtils.setField(field, member, id);
        return member;
    }

    public static Member createMember(String email, String password, Integer age) {
        return new Member(email, password, age);
    }

    public static Favorite createFavorite(Member member, Station sourceStation, Station targetStation) {
        return new Favorite(member, sourceStation, targetStation);
    }
}
