package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("연령별 요금 정책 테스트")
class AgePolicyTest {

    @ParameterizedTest(name = "{displayName} - [{index}] {argumentsWithNames}")
    @CsvSource(value = {"5:INFANT", "6:CHILD", "12:CHILD", "13:YOUTH", "18:YOUTH", "19:ADULT"}, delimiter = ':')
    @DisplayName("연령별 요금 정책을 반환한다.")
    void from(int age, AgePolicy expectedAgePolicy) {
        // when
        AgePolicy agePolicy = AgePolicy.from(age);

        // then
        assertThat(agePolicy).isEqualTo(expectedAgePolicy);
    }

    @ParameterizedTest(name = "{displayName} - [{index}] {argumentsWithNames}")
    @CsvSource(value = {"5:0", "12:450", "18:720", "19:1250"}, delimiter = ':')
    @DisplayName("연령별 할인된 요금을 반환한다.")
    void calculateDiscountedFare(int age, int expectedFare) {
        // given
        AgePolicy agePolicy = AgePolicy.from(age);

        // when
        int discountedFare = agePolicy.calculateDiscountedFare(1250);

        // then
        assertThat(discountedFare).isEqualTo(expectedFare);
    }
}
