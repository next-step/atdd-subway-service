package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeGroupTest {
    @DisplayName("매개변수로 나이가 전달될 때 적절한 연령대(AgeGroup)을 반환하는지 테스트")
    @ParameterizedTest
    @CsvSource(value = {"0:BABY", "6:CHILDREN", "13:TEENAGER", "19:ADULT"}, delimiter = ':')
    void of(int age, AgeGroup expectedAgeGroup) {
        assertThat(AgeGroup.of(age)).isEqualTo(expectedAgeGroup);
    }

    @DisplayName("각 연령대에 요금이 주어졌을 때 할인이 정상적으로 적용되는지 테스트")
    @ParameterizedTest
    @CsvSource(value = {"BABY:1000:0", "CHILDREN:1000:325", "TEENAGER:1000:520", "ADULT:1000:1000"}, delimiter = ':')
    void applyDiscount(AgeGroup ageGroup, int fare, int expectedDiscountedFare) {
        assertThat(ageGroup.applyDiscount(fare)).isEqualTo(expectedDiscountedFare);
    }
}
