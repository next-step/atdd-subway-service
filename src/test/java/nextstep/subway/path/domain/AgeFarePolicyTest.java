package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class AgeFarePolicyTest {

    @DisplayName("나이에 맞는 할인정책 찾기")
    @ParameterizedTest
    @CsvSource(value = {"4:FREE", "6:CHILDREN", "10:CHILDREN", "13:TEENAGER", "16:TEENAGER", "20:GENERAL"}, delimiter = ':')
    void findAgePolicy(int age, String expected) {
        assertThat(AgeFarePolicy.findByAge(age).name()).isEqualTo(expected);
    }

    @Test
    void discountedFare() {
        int fare = 1350;
        assertAll(() -> {
            assertThat(AgeFarePolicy.FREE.discountedFare(fare)).isEqualTo(0);
            assertThat(AgeFarePolicy.CHILDREN.discountedFare(fare)).isEqualTo(500);
            assertThat(AgeFarePolicy.TEENAGER.discountedFare(fare)).isEqualTo(800);
            assertThat(AgeFarePolicy.GENERAL.discountedFare(fare)).isEqualTo(1350);
        });
    }
}
