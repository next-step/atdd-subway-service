package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.station.StationFixture.stationA;
import static nextstep.subway.station.StationFixture.stationB;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("즐겨찾기")
class FavoriteTest {

    @DisplayName("즐겨찾기 생성")
    @Test
    void constructor() {
        Member member = new Member("email", "password", 20);
        assertThatNoException().isThrownBy(() -> new Favorite(member, stationA(), stationB()));
    }
}
