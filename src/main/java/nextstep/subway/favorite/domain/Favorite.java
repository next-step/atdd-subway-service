package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class Favorite {

    private Station source;
    private Station target;
    private Member member;

    public static Favorite from(Station source, Station target) {
        return new Favorite();
    }

    public long id() {
        return 0;
    }

    public Station source() {
        return null;
    }

    public Station target() {
        return null;
    }

    public Member member() {
        return null;
    }
}
