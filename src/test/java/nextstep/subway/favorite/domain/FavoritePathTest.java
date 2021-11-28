package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

class FavoritePathTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @DisplayName("FavoritePath 는 출발역, 도착역, 소유자 정보로 생성할 수 있다.")
    @Test
    void create1() {
        // given
        Station 강남역 = Station.of(1L, "강남역");
        Station 교대역 = Station.of(2L, "교대역");
        Member 사용자 = Member.of(EMAIL, PASSWORD, AGE);

        // when && then
        assertThatNoException().isThrownBy(() -> FavoritePath.of(강남역, 교대역, 사용자));
    }

    @DisplayName("FavoritePath 는 출발역이 없으면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        Station 교대역 = Station.of(2L, "교대역");
        Member 사용자 = Member.of(EMAIL, PASSWORD, AGE);

        // when && then
        assertThatIllegalArgumentException().isThrownBy(() -> FavoritePath.of(null, 교대역, 사용자));
    }

    @DisplayName("FavoritePath 는 도착역이 없으면 예외가 발생한다.")
    @Test
    void create3() {
        // given
        Station 강남역 = Station.of(1L, "강남역");
        Member 사용자 = Member.of(EMAIL, PASSWORD, AGE);

        // when && then
        assertThatIllegalArgumentException().isThrownBy(() -> FavoritePath.of(강남역, null, 사용자));
    }

    @DisplayName("FavoritePath 는 사용자가 없으면 예외가 발생한다.")
    @Test
    void create4() {
        // given
        Station 강남역 = Station.of(1L, "강남역");
        Station 교대역 = Station.of(2L, "교대역");

        // when && then
        assertThatIllegalArgumentException().isThrownBy(() -> FavoritePath.of(강남역, 교대역, null));
    }
}