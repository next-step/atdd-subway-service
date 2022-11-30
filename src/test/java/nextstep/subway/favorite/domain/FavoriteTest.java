package nextstep.subway.favorite.domain;

import nextstep.subway.exception.FavoriteCreateException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.message.ExceptionMessage;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    private Member member;
    private Station sourceStation;
    private Station targetStation;

    @BeforeEach
    void setUp() {
        member = new Member("abc@gmail.com", "password", 10);
        sourceStation = new Station("강남역");
        targetStation = new Station("광교역");
    }

    @DisplayName("즐겨찾기 생성 시 회원이 없으면 예외가 발생한다.")
    @Test
    void createException1() {
        Assertions.assertThatThrownBy(() -> Favorite.of(null, sourceStation, targetStation))
                .isInstanceOf(FavoriteCreateException.class)
                .hasMessageStartingWith(ExceptionMessage.FAVORITE_NOT_HAVE_MEMBER);
    }

    @DisplayName("즐겨찾기 생성 시 출발역이 없으면 예외가 발생한다.")
    @Test
    void createException2() {
        Assertions.assertThatThrownBy(() -> Favorite.of(member, null, targetStation))
                .isInstanceOf(FavoriteCreateException.class)
                .hasMessageStartingWith(ExceptionMessage.FAVORITE_NOT_HAVE_STATION);
    }

    @DisplayName("즐겨찾기 생성 시 도착역이 없으면 예외가 발생한다.")
    @Test
    void createException3() {
        Assertions.assertThatThrownBy(() -> Favorite.of(member, sourceStation, null))
                .isInstanceOf(FavoriteCreateException.class)
                .hasMessageStartingWith(ExceptionMessage.FAVORITE_NOT_HAVE_STATION);
    }

    @DisplayName("즐겨찾기 생성 시 출발역과 도착역이 같으면 예외가 발생한다.")
    @Test
    void createException4() {
        Assertions.assertThatThrownBy(() -> Favorite.of(member, sourceStation, sourceStation))
                .isInstanceOf(FavoriteCreateException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_SOURCE_AND_TARGET_STATION);
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void create() {
        Favorite favorite = Favorite.of(member, sourceStation, targetStation);

        Assertions.assertThat(favorite).isNotNull();
    }
}
