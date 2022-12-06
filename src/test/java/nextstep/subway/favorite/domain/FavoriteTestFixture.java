package nextstep.subway.favorite.domain;

import nextstep.subway.station.domain.Station;

public class FavoriteTestFixture {
    public static Favorite favorite(long memberId, Station departureStation, Station arrivalStation) {
        return Favorite.of(memberId, departureStation, arrivalStation);
    }

    public static Favorite favorite(long id, long memberId, Station departureStation, Station arrivalStation) {
        return Favorite.of(id, memberId, departureStation, arrivalStation);
    }
}
