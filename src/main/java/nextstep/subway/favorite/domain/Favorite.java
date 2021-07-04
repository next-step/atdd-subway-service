package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class Favorite {

    private Member member;
    private Station sourceStation;
    private Station targetStation;

    public Favorite(Member member, Station sourceStation, Station targetStation) {
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }
}
