package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteTest {
    @Test
    @DisplayName("즐겨찾기의 소유자가 다른지 비교한다.")
    void 소유자_비교() {
        Member member = new Member("email@email.com", "1234", 5);
        Member otherMember = new Member("test@gmail.com", "a1a2a3", 5);
        Favorite favorite = new Favorite(new Station("강남역"), new Station("교대역"), member);

        assertThat(favorite.isNotAuthor(otherMember)).isTrue();
        assertThat(favorite.isNotAuthor(member)).isFalse();
    }
}
