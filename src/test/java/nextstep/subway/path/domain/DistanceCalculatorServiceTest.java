package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nextstep.subway.path.domain.calculator.DistanceCalculatorService;

@ExtendWith(SpringExtension.class)
public class DistanceCalculatorServiceTest {

    @InjectMocks
    private DistanceCalculatorService distanceCalculatorService;

    @ParameterizedTest
    @DisplayName("구간별 추가요금 반환. 10km초과∼50km까지(5km마다 100원), 50km초과 시 (8km마다 100원)")
    @MethodSource("ageResultSet")
    void discountRateTest(int distance, int result) {
        assertThat(distanceCalculatorService.getDistanceFare(distance)).isEqualTo(result);
    }

    private static Stream<Arguments> ageResultSet() {
        return Stream.of(
            Arguments.of(10, 0),
            Arguments.of(14, 0),
            Arguments.of(15, 100),
            Arguments.of(20, 200),
            Arguments.of(57, 800),
            Arguments.of(58, 900));
    }
}
