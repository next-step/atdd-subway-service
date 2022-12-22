package nextstep.subway.path.domain;


import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.ExtraFare;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PathInfoCalculatorTest extends PathTestFixture {

    private GraphPath<Station, StationEdge> 경로;

    @BeforeEach
    public void setUp() {
        super.setUp();
        경로 = PathFinder.findPath(노선목록, 강남역, 남부터미널역);
    }

    @DisplayName("경로에 포함된 구간들 거리 합 계산")
    @Test
    void 경로_총_거리_계산() {
        // (강남-양재 10) + (양재-남부터미널 2) = 12
        PathDistance pathDistance = PathInfoCalculator.sumOfEdgeDistance(경로);

        assertThat(pathDistance.getPathDistance()).isEqualTo(12);
    }

    @DisplayName("거리에 따른 기본요금 계산")
    @ParameterizedTest
    @CsvSource({"12,1350", "16,1450", "51,1850"})
    void 기본요금_계산(int input, double expected) {
        // 거리 : 12 -> 1,250 + 100 = 1,350
        // 거리 : 16 -> 1,250 + 200 = 1,450
        // 거리 : 51 -> 1,250 + (100*6) = 1,850
        Fare fare = PathInfoCalculator.calculateMinimumFare(new PathDistance(input));

        assertThat(fare.getFare()).isEqualTo(expected);
    }

    @DisplayName("경로에 포함된 노선들 중 추가요금 지정된 경우 가장 높은 금액 선택")
    @Test
    void 노선_추가요금_확인() {
        ExtraFare extraFare = PathInfoCalculator.getHighstExtraFareOfLines(경로);

        assertThat(extraFare.getExtraFare()).isEqualTo(900);
    }
}
