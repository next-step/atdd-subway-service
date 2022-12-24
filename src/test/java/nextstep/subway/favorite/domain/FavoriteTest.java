package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FavoriteTest {

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void add_favorite() {
        Member member = new Member("test@email.com", "1234", 1);
        Station source = new Station("강남역");
        Station target = new Station("삼성역");
        Favorite favorite = new Favorite(source, target, member);

        assertThat(favorite.getSource()).isEqualTo(source);
        assertThat(favorite.getTarget()).isEqualTo(target);

    }
}
