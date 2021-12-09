package nextstep.subway.favorite.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

public class FavoriteTest {


    @Test
    void equalsFavorite() {

        Station 서울역 = new Station("서울역");
        Station 용산역 = new Station("용산역");

        Member 사용자 = new Member("email@email.com", "password", 20);
        Member 신명진 = new Member("shinmj@email.com", "password", 20);

        Favorite favorite = Favorite.of(서울역, 용산역, 사용자);

        assertTrue(favorite.equalsFavorite(Favorite.of(서울역, 용산역, 사용자)));
        assertFalse(favorite.equalsFavorite(Favorite.of(용산역, 서울역, 사용자)));
        assertFalse(favorite.equalsFavorite(Favorite.of(서울역, 용산역, 신명진)));
    }
}
