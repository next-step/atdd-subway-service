package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FareTest {

    @DisplayName("노선 추가요금(Fare)는 0 이상의 요금으로 생성된다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 100, 1000})
    void create1(int fare) {
        // when & then
        assertThatNoException().isThrownBy(() -> Fare.from(fare));
    }

    @DisplayName("노선 추가요금(Fare)는 음수 요금으로 만들면 예외가 발생한다. ")
    @ParameterizedTest
    @ValueSource(ints = {-1, -10, - 100})
    void create2(int fare) {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Fare.from(fare));
    }
}