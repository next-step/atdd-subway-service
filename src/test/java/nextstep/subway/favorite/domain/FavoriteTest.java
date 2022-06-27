package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    @Test
    @DisplayName("멤버가 없으면 즐겨찾기 할 수 없다.")
    void noMemberFavorite() {
        Station source = new Station("인천역");
        Station target = new Station("인천역");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Favorite(null, source, target)
                );
    }

    @Test
    @DisplayName("역이 없으면 즐겨찾기 할 수 없다.")
    void stationIsEmpty() {
        Station source = new Station("인천역");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Favorite(new Member(), source, null)
                );
    }

    @Test
    @DisplayName("출발역과 도착역이 같은역은 즐겨찾기 할 수 없다.")
    void sameSourceTargetStation() {
        Station source = new Station("인천역");
        Station target = new Station("인천역");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Favorite(new Member(), source, target)
        );
    }

}