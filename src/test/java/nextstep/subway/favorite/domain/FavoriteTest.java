package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void add_favorite() {
        Member member = new Member("test@email.com", "1234", 1);
        Station source = new Station("강남역");
        Station target = new Station("삼성역");
        Favorite favorite = new Favorite(member, source, target);

        assertThat(favorite.getSource()).isEqualTo(source);
        assertThat(favorite.getTarget()).isEqualTo(target);
        assertThat(favorite.getMember()).isEqualTo(member);
    }
}
