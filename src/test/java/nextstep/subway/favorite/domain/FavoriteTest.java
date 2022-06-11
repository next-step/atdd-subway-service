package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Favorite 클래스 테스트")
public class FavoriteTest {

    private Station 강남역;
    private Station 역삼역;
    private Member 사용자;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");

        사용자 = new Member(1L, "heowc1992@gmail.com", "password", 31);
    }

    @DisplayName("Favorite를 생성한다.")
    @Test
    void successfulCreate() {
        Favorite favorite = new Favorite(사용자.getId(), 강남역, 역삼역);

        assertThat(favorite).isNotNull();
    }

    @DisplayName("출발지와 목적지가 같아 Favorite를 생성할 수 없다.")
    @Test
    void failureCreateWithSameStation() {
        assertThatThrownBy(() -> {
            new Favorite(사용자.getId(), 강남역, 강남역);
        }).isInstanceOf(CannotCreatingFavoriteException.class)
        .hasMessageContaining("출발지와 목적지가 같을 수 없습니다.");
    }
}
