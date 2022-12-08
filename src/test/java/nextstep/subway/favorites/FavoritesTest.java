package nextstep.subway.favorites;

import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoritesTest {

    private static final Member MEMBER = new Member("test@test.com", "1234", 20);
    private static final Station 강남역 = new Station("강남역");
    private static final Station 역삼역 = new Station("역삼역");

    @DisplayName("동일성 테스트")
    @Test
    void equalsTest() {
        assertThat(new Favorites(강남역, 역삼역, MEMBER)).isEqualTo(new Favorites(강남역, 역삼역, MEMBER));
    }

    @DisplayName("출발역 도착역 동일성 테스트")
    @Test
    void sourceTargetEqualExceptionTest() {
        assertThatThrownBy(() -> new Favorites(강남역, 강남역, MEMBER))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
