package nextstep.subway.favorite.domain;

import javax.persistence.EntityNotFoundException;
import nextstep.subway.common.exception.ErrorEnum;
import nextstep.subway.member.domain.Member;
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

    @Test
    void 즐겨찾기_생성() {
        Favorite favorite = Favorite.of(member, sourceStation, targetStation);

        Assertions.assertThat(favorite).isNotNull();
    }

    @DisplayName("즐겨찾기 생성 시 회원이 없으면 예외가 발생한다.")
    @Test
    void createException1() {
        Assertions.assertThatThrownBy(() -> Favorite.of(null, sourceStation, targetStation))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ErrorEnum.NOT_EXISTS_MEMBER.message());
    }

    @DisplayName("즐겨찾기 생성 시 출발역이 없으면 예외가 발생한다.")
    @Test
    void createException2() {
        Assertions.assertThatThrownBy(() -> Favorite.of(member, null, targetStation))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ErrorEnum.NOT_EXISTS_STATION.message());
    }

    @DisplayName("즐겨찾기 생성 시 도착역이 없으면 예외가 발생한다.")
    @Test
    void createException3() {
        Assertions.assertThatThrownBy(() -> Favorite.of(member, sourceStation, null))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ErrorEnum.NOT_EXISTS_STATION.message());
    }
}
