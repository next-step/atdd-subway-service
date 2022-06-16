package nextstep.subway.utils;

import java.lang.reflect.Field;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class ReflectionHelper {

    public static void 내정보_ID_설정하기(Long id, Member member)
        throws NoSuchFieldException, IllegalAccessException {
        Field field = Member.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(member, id);
    }

    public static void 역_ID_설정하기(Long id, Station station)
        throws NoSuchFieldException, IllegalAccessException {
        Field field = Station.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(station, id);
    }

}
