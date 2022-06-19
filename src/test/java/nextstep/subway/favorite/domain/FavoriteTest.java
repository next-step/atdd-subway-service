package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    private Member 사용자;
    private Station 강남역;
    private Station 광교역;

    @BeforeEach
    public void setUp() {
        //given
        사용자 = new Member("test@email.com", "password", 20);
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        //when
        Favorite favorite = 즐겨찾기_생성(사용자, 강남역, 광교역);

        //then
        즐겨찾기_생성됨(favorite);
    }

    @DisplayName("같은 역을 출발지와 도착지로 설정하면 즐겨찾기 생성에 실패한다.")
    @Test
    void invalid_createFavorite() {
        //when & then
        assertThatThrownBy(() -> {
            즐겨찾기_생성(사용자, 강남역, 강남역);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발지와 도착지는 다른 역으로 등록해주세요.");
    }

    private Favorite 즐겨찾기_생성(Member member, Station source, Station target) {
        return new Favorite(member, source, target);
    }

    private void 즐겨찾기_생성됨(Favorite favorite) {
        assertThat(favorite).isNotNull();
        assertThat(favorite.getMember()).isEqualTo(사용자);
        assertThat(favorite.getSource()).isEqualTo(강남역);
        assertThat(favorite.getTarget()).isEqualTo(광교역);
    }
}
