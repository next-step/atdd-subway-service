package nextstep.subway.path.domain;

import nextstep.subway.path.domain.fee.distanceFee.DefaultDistanceFee;
import nextstep.subway.path.domain.fee.distanceFee.DistanceFee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFeeCalculatorServiceTest {
    private FeeCalculatorService feeCalculatorService;

    @BeforeEach
    void setup() {
        feeCalculatorService = new FeeCalculatorService();
    }

    @DisplayName("거리에 따른 요금을 계산할 수 있다.")
    @ParameterizedTest
    @MethodSource("calculateByDistanceTestResource")
    void calculateByDistanceResource(Long source, Long target, BigDecimal expected) {
        List<Long> stationIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        List<SafeSectionInfo> safeSectionInfos = Arrays.asList(
                new SafeSectionInfo(1L, 2L, 5),
                new SafeSectionInfo(2L, 3L, 6),
                new SafeSectionInfo(2L, 4L, 5),
                new SafeSectionInfo(2L, 5L, 46)
        );

        PathFinder pathFinder = PathFinder.of(stationIds, safeSectionInfos);
        ShortestPath shortestPath = pathFinder.findShortestPath(source, target);

        DistanceFee distanceFee = feeCalculatorService.calculateDistanceFee(shortestPath);

        assertThat(distanceFee.calculate()).isEqualTo(expected);
    }
    public static Stream<Arguments> calculateByDistanceTestResource() {
        return Stream.of(
                Arguments.of(1L, 4L, BigDecimal.valueOf(1250)),
                Arguments.of(1L, 3L, BigDecimal.valueOf(1350)),
                Arguments.of(1L, 5L, BigDecimal.valueOf(2150))
        );
    }
}