package nextstep.subway.member.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;

class MemberTest {
    private Station 강남역;
    private Station 양재역;
    private Member 사용자1;
    private Favorite 즐겨찾기;

    @BeforeEach
    void setUp() {
        // given
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(2L, "양재역");
        사용자1 = new Member(1L, "mslim@naver.com", "password", 20);
        즐겨찾기 = new Favorite(강남역, 양재역, 사용자1);
    }

    @Test
    void addFavorite() {
        // given
        final Member 사용자2 = new Member(2L, "email@email.com", "asdf", 1);

        // when
        사용자2.addFavorite(즐겨찾기);

        // then
        assertThat(즐겨찾기.getOwner()).isNotEqualTo(사용자1);
        assertThat(즐겨찾기.getOwner()).isEqualTo(사용자2);
        assertThat(사용자1.getFavorites()).doesNotContain(즐겨찾기);
        assertThat(사용자2.getFavorites()).contains(즐겨찾기);
    }
}
