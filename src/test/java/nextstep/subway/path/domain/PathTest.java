package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

    private Station 강남역;
    private Station 역삼역;
    private Station 선릉역;
    private Path path;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");
        path = new Path(Arrays.asList(강남역, 역삼역, 선릉역), new Distance(20), 0);
    }

    @Test
    @DisplayName("거리에 따른 요금을 계산한다")
    void 거리에_따른_요금_계산() {
        assertThat(path.getFare()).isEqualTo(1450);
    }

    @Test
    @DisplayName("어린이 요금을 계산한다")
    void 어린이_요금_계산() {
        LoginMember member = new LoginMember(0L, "email@email.com", 9);
        path.calculateFare(member);
        assertThat(path.getFare()).isEqualTo(550);
    }

    @Test
    @DisplayName("청소년 요금을 계산한다")
    void 청소년_요금_계산() {
        LoginMember member = new LoginMember(0L, "email@email.com", 16);
        path.calculateFare(member);
        assertThat(path.getFare()).isEqualTo(880);
    }

    @Test
    @DisplayName("성인 요금을 계산한다")
    void 성인_요금_계산() {
        LoginMember member = new LoginMember(0L, "email@email.com", 20);
        path.calculateFare(member);
        assertThat(path.getFare()).isEqualTo(1450);
    }

    @Test
    @DisplayName("비회원 요금을 계산한다")
    void 비회원_요금_계산() {
        path.calculateFare(null);
        assertThat(path.getFare()).isEqualTo(1450);
    }
}
