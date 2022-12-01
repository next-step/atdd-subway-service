package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class FavoriteTestFixture {
    public static Favorite favorite(Member member, Station departureStation, Station arrivalStation) {
        return Favorite.of(member, departureStation, arrivalStation);
    }
}
