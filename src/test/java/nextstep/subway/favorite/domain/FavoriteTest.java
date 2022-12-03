package nextstep.subway.favorite.domain;

import javax.persistence.EntityNotFoundException;
import nextstep.subway.common.exception.ErrorEnum;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    @Test
    void 즐겨찾기_생성시_회원정보_없으면_예외() {
        Assertions.assertThatThrownBy(() -> Favorite.of(null, sourceStation, targetStation))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ErrorEnum.NOT_EXISTS_MEMBER.message());
    }

    @Test
    void 즐겨찾기_생성시_출발역_없으면_예외() {
        Assertions.assertThatThrownBy(() -> Favorite.of(member, null, targetStation))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ErrorEnum.NOT_EXISTS_STATION.message());
    }

    @Test
    void 즐겨찾기_생성시_도착역_없으면_예외() {
        Assertions.assertThatThrownBy(() -> Favorite.of(member, sourceStation, null))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ErrorEnum.NOT_EXISTS_STATION.message());
    }
}
