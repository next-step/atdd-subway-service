package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgePolicyTest {

    @DisplayName("청소년인지 확인한다.")
    @ParameterizedTest
    @CsvSource({"10, false", "13, true", "15, true", "19, false", "20, false"})
    void isTeenager_test(int age, boolean expected) {
        //when
        boolean isTeenager = AgePolicy.isTeenager(age);

        //then
        assertThat(isTeenager).isEqualTo(expected);
    }

    @DisplayName("어린이인지 확인한다.")
    @ParameterizedTest
    @CsvSource({"3, false", "6, true", "10, true", "13, false", "15, false"})
    void isChild_test(int age, boolean expected) {
        //when
        boolean isTeenager = AgePolicy.isChild(age);

        //then
        assertThat(isTeenager).isEqualTo(expected);
    }
}
