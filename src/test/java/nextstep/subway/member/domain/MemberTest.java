package nextstep.subway.member.domain;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @DisplayName("즐겨찾기 목록을 추가해보자")
    @Test
    void addFavorite() {
        Member member = new Member("test@email.com", "password", 20);

        Station 강남역 = new Station("강남역");
        Station 잠실역 = new Station("잠실역");

        Favorite favorite = new Favorite(강남역, 잠실역);
        member.addFavorite(favorite);

        assertThat(member.getFavorites()).containsExactly(favorite);
        assertThat(favorite.getMember()).isEqualTo(member);
    }

    @DisplayName("즐겨찾기 목록을 석제해보자")
    @Test
    void removeFavorite() {
        // given
        Member member = new Member("test@email.com", "password", 20);

        Station 강남역 = new Station("강남역");
        Station 잠실역 = new Station("잠실역");

        Favorite favorite = new Favorite(강남역, 잠실역);
        member.addFavorite(favorite);

        // when
        member.removeFavorite(favorite);

        // then
        assertThat(member.getFavorites()).hasSize(0);
    }

}