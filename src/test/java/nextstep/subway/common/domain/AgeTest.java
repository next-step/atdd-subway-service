package nextstep.subway.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("나이")
class AgeTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Age.from(1));
    }

    @Test
    @DisplayName("나이는 반드시 양수")
    void instance_negativeValue_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Age.from(Integer.MIN_VALUE))
            .withMessage("나이는 반드시 양수이어야 합니다.");
    }


    @ParameterizedTest(name = "[{index}] {0} 나이는 어린이라는 사실이 {1}")
    @DisplayName("어린이 판별")
    @CsvSource({"6,true", "12,true", "13,false"})
    void isChild(int age, boolean expected) {
        // when
        boolean isChild = Age.from(age).isChild();

        // then
        assertThat(isChild).isEqualTo(expected);
    }

    @ParameterizedTest(name = "[{index}] {0} 나이는 청소년이라는 사실이 {1}")
    @DisplayName("청소년 판별")
    @CsvSource({"12,false", "13,true", "18,true", "19,false"})
    void isYouth(int age, boolean expected) {
        // when
        boolean isYouth = Age.from(age).isYouth();

        // then
        assertThat(isYouth).isEqualTo(expected);
    }
}
