package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoriteTest {
    @Test
    @DisplayName("즐겨찾기 추가 실패 (동일한 역으로 추가)")
    void validate_empty() {
        // given
        Member member = new Member("las139@email.com", "1234", 20);
        Station station = new Station("강남역");

        // then
        assertThatThrownBy(() -> {
            Favorite.of(member, station, station);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
