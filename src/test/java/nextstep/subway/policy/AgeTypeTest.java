package nextstep.subway.policy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeTypeTest {

    @DisplayName("어린이 요금 정보")
    @ParameterizedTest
    @ValueSource(ints = {
            6, 12
    })
    void childAgeTypeTest(int age) {
        assertAgeType(age, AgeType.CHILD);
    }

    @DisplayName("청소년 요금 정보")
    @ParameterizedTest
    @ValueSource(ints = {
            13, 18
    })
    void youthAgeTypeTest(int age) {
        assertAgeType(age, AgeType.YOUTH);
    }

    @DisplayName("성인 요금 정보")
    @ParameterizedTest
    @ValueSource(ints = {
            19, 64
    })
    void adultAgeTypeTest(int age) {
        assertAgeType(age, AgeType.ADULT);
    }

    @DisplayName("우대 요금 정보")
    @ParameterizedTest
    @ValueSource(ints = {
            0, 5 , 65
    })
    void otherAgeTypeTest(int age) {
        assertAgeType(age, AgeType.OTHER);
    }

    private void assertAgeType(int age, AgeType type) {
        AgeType actual = AgeType.of(age);

        assertThat(actual).isEqualTo(type);
    }

    @DisplayName("나이별 할인된 요금")
    @ParameterizedTest
    @CsvSource(value = {
            "1250, 19, 1250", "1250, 13, 720", "1250, 6, 450", "1250, 5, 0", "1250, 65, 0"
    })
    void discountedFareByAgeTest(int fare, int age, int expected) {
        AgeType ageType = AgeType.of(age);
        System.out.println(ageType);
        int discountFare = ageType.discount(fare);

        assertThat(discountFare).isEqualTo(expected);
    }

}