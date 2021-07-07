package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class FavoriteTest {

    @Test
    @DisplayName("생성 테스트")
    void create() {
        // given
        Member 회원 = new Member("ehdgml3206@gmail.com", "1234", 31);
        Station 강남역 = new Station("강남역");
        Station 판교역 = new Station("판교역");

        // when
        Favorite favorite = Favorite.of(1L, 회원, 강남역, 판교역);

        // then
        assertThat(favorite.getMember()).isEqualTo(회원);
        assertThat(favorite.getSource()).isEqualTo(강남역);
        assertThat(favorite.getTarget()).isEqualTo(판교역);
    }

    @Test
    @DisplayName("즐겨찾기 소유자 비교 테스트")
    void isOwner() {
        // given
        Member 회원 = new Member(1L, "ehdgml3206@gmail.com", "1234", 31);
        Member 다른회원 = new Member(2L, "ehdgml3206@gmail.com", "1234", 31);
        Station 강남역 = new Station("강남역");
        Station 판교역 = new Station("판교역");

        // when
        Favorite favorite = Favorite.of(1L, 회원, 강남역, 판교역);

        // then
        assertThat(favorite.isOwner(회원.getId())).isTrue();
        assertThat(favorite.isOwner(다른회원.getId())).isFalse();
    }
}
