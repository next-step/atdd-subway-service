package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FavoriteTest {

    @Test
    void create() {
        Station source = new Station("신도림역");
        Station target = new Station("노량진역");

        assertThat(new Favorite(new Member(), source, target));
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 등록에 실패하는 테스트")
    void SourceEqualsTarget() {
        Station source = new Station("신도림역");
        Station target = new Station("신도림역");


        assertThatThrownBy(() -> new Favorite(new Member(), source, target));
    }

}