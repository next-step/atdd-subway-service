package nextstep.subway.favorite.domain;

import nextstep.subway.favorite.application.exception.InvalidFavoriteException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("즐겨찾기 기능")
class FavoriteTest {
    private Member 회원;
    private Station 이수역;
    private Station 사당역;

    @BeforeEach
    public void setUp() {
        회원 = new Member("email", "password", 50);
        이수역 = new Station(1L, "이수역");
        사당역 = new Station(2L, "사당역");
    }

    @Test
    @DisplayName("즐겨찾기를 생성한다.")
    void create() {
        // given when
        Favorite favorite = new Favorite(회원, 이수역, 사당역);

        // then
        assertThat(favorite).extracting("source", "target")
                .containsExactly(이수역, 사당역);
    }

    @Test
    @DisplayName("즐겨찾기의 출발역과 도착역이 같은 경우 예외가 발생한다.")
    void validateSameSourceTarget() {
        // given when then
        assertThatThrownBy(() -> new Favorite(회원, 이수역, 이수역))
                .isInstanceOf(InvalidFavoriteException.class);
    }
}
