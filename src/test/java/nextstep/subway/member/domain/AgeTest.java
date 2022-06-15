package nextstep.subway.member.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import nextstep.subway.fare.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AgeTest {
    @DisplayName("음수로 나이를 초기화하면 IllegalArgumentException 예외")
    @ParameterizedTest(name = "나이 {0}로 나이를 초기화하면 IllegalArgumentException 예외")
    @ValueSource(ints = {-1, -100, -10000})
    void fareNegativeNumber(int input) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Fare.valueOf(input))
                .withMessage("음수는 유효하지 않습니다.");
    }
}
