package nextstep.subway.auth.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeTest {

    @ParameterizedTest(name = "청소년 나이 검증 테스트")
    @CsvSource(value = {"13:true", "18:true", "19:false"}, delimiter = ':')
    void isYouth(int age, boolean actual) {
        Age ageResult = Age.of(age);

        Assertions.assertThat(ageResult.isYouth()).isEqualTo(actual);
    }

    @ParameterizedTest(name = "어린이 나이 검증 테스트")
    @CsvSource(value = {"6:true", "8:true", "12:true", "13:false"}, delimiter = ':')
    void isChild(int age, boolean actual) {
        Age ageResult = Age.of(age);

        Assertions.assertThat(ageResult.isChild()).isEqualTo(actual);
    }
}
