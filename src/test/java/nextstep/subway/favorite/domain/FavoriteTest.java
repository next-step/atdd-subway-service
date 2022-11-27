package nextstep.subway.favorite.domain;

import nextstep.subway.constant.ErrorCode;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoriteTest {
    @DisplayName("즐겨찾기의 출발지 도착지가 동일하면 예외가 발생한다")
    @Test
    void sameSourceTargetException() {
        // given
        Member member = new Member("email", "password", 33);
        Station station = new Station("판교역");

        // when & then
        assertThatThrownBy(() -> new Favorite(member, station, station))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.FAVORITE_SAME_SOURCE_TARGET.getMessage());
    }
}
