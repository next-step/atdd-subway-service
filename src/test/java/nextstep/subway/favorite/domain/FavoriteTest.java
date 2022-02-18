package nextstep.subway.favorite.domain;

import static org.junit.jupiter.api.Assertions.*;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.StationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


class FavoriteTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 10;
    private static final String SAME_STATIONS_EXCEPTION = "즐겨찾기에 추가하려는 출발역과 도착역을 같을 수 없습니다.";
    private static final String UNAUTHENTICATED_EXCEPTION = "해당 동작에 대한 권한이 존재하지 않습니다.";

    @DisplayName("즐겨찾기는 출발역과 도착역이 같을 수 없다.")
    @Test
    void favorite_with_same_source_and_target_is_invalid() {
        assertThatThrownBy(() -> Favorite.of(new Member(EMAIL, PASSWORD, AGE), StationTest.STATION_4, StationTest.STATION_4))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(SAME_STATIONS_EXCEPTION);
    }

    @DisplayName("즐겨찾기에 등록된 사용자 정보와 일치하지 않는 사용자는 권한이 없다.")
    @Test
    void unAuthenticated_member_has_no_authority() {
        Favorite favorite = Favorite.of(new Member(1L, EMAIL, PASSWORD, AGE), StationTest.STATION_4, StationTest.STATION_5);
        assertThatThrownBy(() -> favorite.checkAuthority(2L))
            .isInstanceOf(AuthorizationException.class)
            .hasMessage(UNAUTHENTICATED_EXCEPTION);
    }


}