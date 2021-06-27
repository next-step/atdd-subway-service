package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteTest {
    private static final Station 강남역 = Station.of(1L, "강남역");
    private static final Station 판교역 = Station.of(2L, "판교역");
    private static final Member 유나 = new Member("email", "password", 29);
    private static final Member 김연아 = new Member("email", "password", 32);

    @DisplayName("출발역과 도착역이 같으면 즐겨찾기 추가시 예외가 발생한다.")
    @Test
    void validateNotEqualsSourceAndTarget() {
        assertThatThrownBy(() -> Favorite.of(유나, 강남역, 강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("동일한 역으로 즐겨찾기를 추가할 수 없습니다.");
    }

    @DisplayName("동일한 사용자가 아니면 예외가 발생한다.")
    @Test
    void checkSameMember() {
        // given
        Favorite favorite = Favorite.of(유나, 강남역, 판교역);

        // when & then
        assertThatThrownBy(() -> favorite.checkSameMember(김연아))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("즐겨찾기를 추가한 사용자가 아닙니다.");
    }
}
