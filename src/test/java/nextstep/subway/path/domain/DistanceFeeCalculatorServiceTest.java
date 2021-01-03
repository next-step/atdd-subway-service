package nextstep.subway.path.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFeeCalculatorServiceTest {
    private FeeCalculatorService feeCalculatorService;

    @BeforeEach
    void setup() {
        feeCalculatorService = new FeeCalculatorService();
    }

    @DisplayName("거리에 따른 요금을 계산할 수 있다.")
    @Test
    void calculateByDistance() {
//        PathFinder pathFinder = new PathFinder();
//        ShortestPath shortestPath = pathFinder.findShortestPath();
//
//        BigDecimal distanceFee = feeCalculatorService.calculateDistanceFee(shortestPath);
//
//        assertThat(distanceFee)
    }
}