package study.unit;

import nextstep.subway.line.domain.CostType;

import static nextstep.subway.line.domain.CostType.*;
import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("요금 관련 테스트")
public class CostTest {
    private static final int DEFAULT_COST = 1250;
    private static final int DEFAULT_DISTANCE = 10;
    private static final int EXPECTED_YOUTH_RESULT_CASE_01 = (int) (DEFAULT_COST - 350 + 1100 - ((DEFAULT_COST - 350 + 1100) * 0.2));
    private static final int EXPECTED_YOUTH_RESULT_CASE_02 = (int) (DEFAULT_COST - 350 + 1000 - ((DEFAULT_COST - 350 + 1000) * 0.2));
    private static final int EXPECTED_YOUTH_RESULT_CASE_03 = (int) (DEFAULT_COST - 350 - ((DEFAULT_COST - 350) * 0.2));
    private static final int EXPECTED_CHILD_RESULT_CASE_01 = (int) (DEFAULT_COST - 350 + 1100 - ((DEFAULT_COST - 350 + 1100) * 0.5));
    private static final int EXPECTED_CHILD_RESULT_CASE_02 = (int) (DEFAULT_COST - 350 + 1000 - ((DEFAULT_COST - 350 + 1000) * 0.5));
    private static final int EXPECTED_CHILD_RESULT_CASE_03 = (int) (DEFAULT_COST - 350 - ((DEFAULT_COST - 350) * 0.5));

    @DisplayName("요금 계산 테스트")
    @ParameterizedTest
    @MethodSource("arguments")
    void costTest(CostType type, int distance, int result) {
        assertThat(type.getFare(distance)).isEqualTo(result);
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(Arguments.of(ADULT, 68, DEFAULT_COST + 1100),
                Arguments.of(ADULT, 60, DEFAULT_COST + 1000),
                Arguments.of(ADULT, DEFAULT_DISTANCE, DEFAULT_COST),
                Arguments.of(YOUTH, 68, EXPECTED_YOUTH_RESULT_CASE_01),
                Arguments.of(YOUTH, 60, EXPECTED_YOUTH_RESULT_CASE_02),
                Arguments.of(YOUTH, DEFAULT_DISTANCE, EXPECTED_YOUTH_RESULT_CASE_03),
                Arguments.of(CHILD, 68, EXPECTED_CHILD_RESULT_CASE_01),
                Arguments.of(CHILD, 60, EXPECTED_CHILD_RESULT_CASE_02),
                Arguments.of(CHILD, DEFAULT_DISTANCE, EXPECTED_CHILD_RESULT_CASE_03),
                Arguments.of(FREE, DEFAULT_DISTANCE, 0));
    }
}
