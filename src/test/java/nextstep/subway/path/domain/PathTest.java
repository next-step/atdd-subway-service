package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철 경로 클래스 테스트")
class PathTest {

    private Station 강남역;
    private Station 역삼역;
    private Station 선릉역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");
    }

    @Test
    void 거리에_따른_운임_비용_계산() {
        Path path = new Path(Arrays.asList(강남역, 역삼역, 선릉역), 20);
        assertEquals(1450, path.getFare());
    }

    @Test
    void 지하철_운임_비용_계산() {
        LoginMember loginMember = new LoginMember(1L, "testuser@test.com", 6);
        Path path = new Path(Arrays.asList(강남역, 역삼역, 선릉역), 20);
        path.calculateFare(loginMember,
                new Lines(Arrays.asList(new Line("2호선", "bg-green-600", 100))));

        assertEquals(600, path.getFare());
    }
}
