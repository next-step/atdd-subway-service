package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.adapters.SafeLineAdapter;
import nextstep.subway.path.domain.fee.transferFee.LineOfStationInPath;
import nextstep.subway.path.domain.fee.transferFee.LineOfStationInPaths;
import nextstep.subway.path.domain.fee.transferFee.LineWithExtraFee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DistanceFeeCalculatorServiceTest {
    private FeeCalculatorService feeCalculatorService;

    @Mock
    private SafeLineAdapter safeLineAdapter;

    @BeforeEach
    void setup() {
        feeCalculatorService = new FeeCalculatorService(safeLineAdapter);
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

        BigDecimal distanceFee = feeCalculatorService.calculateDistanceFee(shortestPath);

        assertThat(distanceFee).isEqualTo(expected);
    }
    public static Stream<Arguments> calculateByDistanceTestResource() {
        return Stream.of(
                Arguments.of(1L, 4L, BigDecimal.valueOf(1250)),
                Arguments.of(1L, 3L, BigDecimal.valueOf(1350)),
                Arguments.of(1L, 5L, BigDecimal.valueOf(2150))
        );
    }

    @DisplayName("거리, 환승여부에 따른 요금을 계산하고 할인율을 적용할 수 있다.")
    @Test
    void calculateExtraFeeTest() {
        Long source = 1L;
        Long target = 3L;
        LoginMember loginMember = new LoginMember(1L, "test@nextstep.com", 15);
        BigDecimal expectedFee = BigDecimal.valueOf(808);

        List<Long> stationIds = Arrays.asList(1L, 2L, 3L, 4L);
        List<SafeSectionInfo> safeSectionInfos = Arrays.asList(
                new SafeSectionInfo(1L, 2L, 5),
                new SafeSectionInfo(2L, 3L, 6),
                new SafeSectionInfo(2L, 4L, 5)
        );

        PathFinder pathFinder = PathFinder.of(stationIds, safeSectionInfos);
        ShortestPath shortestPath = pathFinder.findShortestPath(source, target);

        LineOfStationInPaths transferOnce = new LineOfStationInPaths(Arrays.asList(
                new LineOfStationInPath(Collections.singletonList(new LineWithExtraFee(1L, BigDecimal.ZERO))),
                new LineOfStationInPath(Arrays.asList(new LineWithExtraFee(1L, BigDecimal.ZERO), new LineWithExtraFee(2L, BigDecimal.TEN))),
                new LineOfStationInPath(Collections.singletonList(new LineWithExtraFee(2L, BigDecimal.TEN)))
        ));

        given(safeLineAdapter.getLineOfStationInPaths(Arrays.asList(1L, 2L, 3L))).willReturn(transferOnce);

        BigDecimal extraFee = feeCalculatorService.calculateExtraFee(shortestPath, loginMember);

        assertThat(extraFee).isEqualTo(expectedFee);
    }
}