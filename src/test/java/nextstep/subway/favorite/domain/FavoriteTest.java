package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기에 대한 도메인테스트")
class FavoriteTest {

    private Station 강남역;
    private Station 잠실역;

    private Member 회원;

    @BeforeEach
    void before() {
        강남역 = new Station("강남역");
        잠실역 = new Station("잠실역");

        회원 = new Member("email@email.com", "password", 30);
    }

    @Test
    @DisplayName("즐겨찾기의 source, target이 같다면 예외")
    void test1() {
        assertThatThrownBy(
                () -> {
                    Favorite favorite = new Favorite(회원, 강남역, 강남역);
                }
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
