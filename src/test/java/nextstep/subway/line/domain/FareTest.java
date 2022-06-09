package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FareTest {

    @DisplayName("0 이상의 값으로 Fare 도메인을 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100, 1000, 10000})
    void create01(int fare) {
        // given & when
        Fare createdFare = Fare.from(fare);

        // then
        assertThat(createdFare.getValue()).isEqualTo(fare);
    }

    @DisplayName("0 미만의 값으로 Fare 도메인을 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {-100, -10, -1})
    void create02(int fare) {
        // given & when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Fare.from(fare))
                .withMessageContaining(LineExceptionType.LINE_FARE_IS_OVER_ZERO.getMessage());
    }
}