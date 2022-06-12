package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class FavoriteTest {

    private Station 신촌역;
    private Station 홍대입구역;
    private LoginMember 사용자;

    @BeforeEach
    void setUp() {
        신촌역 = new Station("신촌역");
        홍대입구역 = new Station("홍대입구역");
        사용자 = new LoginMember(1L, "email@email.com", 40);
    }

    @Test
    void 클래스_명세_정의() {
        //id, memberId, source, target
        Favorite favorite = 경로_즐겨찾기를_생성한다(사용자.getId(), 신촌역, 홍대입구역);
        ReflectionTestUtils.setField(favorite, "id", 1L);

        assertThat(favorite.getId()).isInstanceOf(Long.class);
        assertThat(favorite.getMemberId()).isInstanceOf(Long.class);
        assertThat(favorite.getSource()).isInstanceOf(Station.class);
        assertThat(favorite.getTarget()).isInstanceOf(Station.class);
    }


    @Test
    void 경로_즐겨찾기_생성_memberId가_없는_경우_예외() {
        assertThatThrownBy(() -> {
            경로_즐겨찾기를_생성한다(null, 신촌역, 홍대입구역);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 경로_즐겨찾기_생성_출발역이_없는_경우_예외() {
        Station 유령역 = null;
        assertThatThrownBy(() -> {
            경로_즐겨찾기를_생성한다(사용자.getId(), 신촌역, 유령역);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 경로_즐겨찾기_생성_도착역이_없는_경우_예외() {
        Station 유령역 = null;
        assertThatThrownBy(() -> {
            경로_즐겨찾기를_생성한다(사용자.getId(), 유령역, 신촌역);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    private Favorite 경로_즐겨찾기를_생성한다(Long memberId, Station source, Station target) {
        return new Favorite(memberId, source, target);
    }
}
