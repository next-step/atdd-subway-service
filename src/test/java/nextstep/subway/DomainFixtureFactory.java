package nextstep.subway;

import java.util.ArrayList;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Path;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class DomainFixtureFactory {
    public static Station createStation(String name) {
        return Station.builder(name)
                .build();
    }

    public static Station createStation(long id, String name) {
        return Station.builder(name)
                .id(id)
                .build();
    }

    public static Section createSection(long id, Line line, Station upStation, Station downStation, Distance distance) {
        return Section.builder(line, upStation, downStation, distance)
                .id(id)
                .build();
    }

    public static Section createSection(Line line, Station upStation, Station downStation, Distance distance) {
        return Section.builder(line, upStation, downStation, distance)
                .build();
    }

    public static Line createLine(long id, String name, String color, Station upStation, Station downStation,
                                  Distance distance) {
        return Line.builder(name, color, upStation, downStation, distance)
                .id(id)
                .build();
    }

    public static Line createLine(long id, String name, String color, Station upStation, Station downStation,
                                  Distance distance, Fare fare) {
        return Line.builder(name, color, upStation, downStation, distance)
                .id(id)
                .additionalFare(fare)
                .build();
    }

    public static Line createLine(String name, String color, Station upStation, Station downStation,
                                  Distance distance) {
        return Line.builder(name, color, upStation, downStation, distance)
                .build();
    }

    public static LineRequest createLineRequest(String name, String color, long upStationId, long downStationId,
                                                int distance) {
        return new LineRequest(name, color, upStationId, downStationId, distance);
    }

    public static SectionRequest createSectionRequest(long upStationId, long downStationId, int distance) {
        return new SectionRequest(upStationId, downStationId, distance);
    }

    public static LoginMember createLoginMember(long id, String email, Age age) {
        return new LoginMember(id, email, age);
    }

    public static Member createMember(long id, String email, String password, Age age) {
        return new Member(id, email, password, age);
    }

    public static Favorite createFavorite(long id, Member member, Station source, Station target) {
        return Favorite.builder(member, source, target)
                .id(id)
                .build();
    }

    public static Favorite createFavorite(Member member, Station source, Station target) {
        return Favorite.builder(member, source, target)
                .build();
    }

    public static Path createPath(ArrayList<Station> stations, Distance distance) {
        return Path.valueOf(stations, distance);
    }
}
