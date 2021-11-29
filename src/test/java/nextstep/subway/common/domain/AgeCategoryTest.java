package nextstep.subway.common.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeCategoryTest {

    @ParameterizedTest(name = "[{index}] {0} 나이는 {1}")
    @DisplayName("나이 분류 가져오기")
    @CsvSource({"1,INFANT", "6,CHILD", "14,YOUTH", "19,ADULT"})
    void valueOf(int age, AgeCategory expected) {
        assertThat(AgeCategory.valueOf(age)).isEqualTo(expected);
    }

}
