package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.favorite.domain.Favorite.TARGET_SOURCE_SAME_EXCEPTION_MESSAGE;
import static nextstep.subway.station.StationFixture.stationA;
import static nextstep.subway.station.StationFixture.stationB;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("즐겨찾기")
class FavoriteTest {

    @DisplayName("즐겨찾기 생성")
    @Test
    void constructor() {
        Member member = new Member("email", "password", 20);
        assertThatNoException().isThrownBy(() -> new Favorite(member, stationA(), stationB()));
    }

    @DisplayName("출발역과 도착역이 같을 수 없다.")
    @Test
    void name() {
        Member member = new Member("email", "password", 20);
        Station stationA = new Station(1L, "A");
        assertThatThrownBy(() -> new Favorite(member, stationA, stationA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(TARGET_SOURCE_SAME_EXCEPTION_MESSAGE);
    }
}
