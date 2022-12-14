package nextstep.subway.fare.application;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fixture.LineFixture;
import nextstep.subway.fixture.StationFixture;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPathFinder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class FareServiceTest {

    private final FareService fareService = new FareService();

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재  --- *3호선* --- 오금역
     *
     * 신분당선은 700원의 추가 요금이 있음
     */

    @DisplayName("10km 이내 거리에 해당되는 요금을 계산하여 반환한다")
    @Test
    void calculate_fare_with_none_policy() {
        // given
        PathFinder pathFinder = new ShortestPathFinder(Arrays.asList(LineFixture.이호선, LineFixture.삼호선, LineFixture.신분당선));
        // 최단 경로 : 남부터미널역 -*3호선*- 양재
        Path path = pathFinder.findPath(StationFixture.남부터미널역, StationFixture.양재역);

        // when
        Fare fare = fareService.calculateFare(path);

        // then
        assertThat(fare).isEqualTo(Fare.of(1_250));
    }

    @DisplayName("10km ~ 50km 거리에 해당되는 요금을 계산하여 반환한다")
    @Test
    void calculate_fare_with_over_ten_policy() {
        // given
        PathFinder pathFinder = new ShortestPathFinder(Arrays.asList(LineFixture.이호선, LineFixture.삼호선, LineFixture.신분당선));
        // 최단 경로 : 남부터미널역 -*3호선*- 양재 -*신분당선*- 강남역
        // 신분당선은 700원의 추가 요금이 있음
        Path path = pathFinder.findPath(StationFixture.강남역, StationFixture.남부터미널역);

        // when
        Fare fare = fareService.calculateFare(path);

        // then
        assertThat(fare).isEqualTo(Fare.of(2_150));
    }

    @DisplayName("50km 초과 거리에 해당되는 요금을 계산하여 반환한다")
    @Test
    void calculate_fare_with_over_fifty_policy() {
        // given
        PathFinder pathFinder = new ShortestPathFinder(Arrays.asList(LineFixture.이호선, LineFixture.삼호선, LineFixture.신분당선));
        // 최단 경로 : 교대역 -*3호선*- 남부터미널역 -*3호선*- 양재 -*3호선*- 오금역
        Path path = pathFinder.findPath(StationFixture.교대역, StationFixture.오금역);

        // when
        Fare fare = fareService.calculateFare(path);

        // then
        assertThat(fare).isEqualTo(Fare.of(2_250));
    }
}
