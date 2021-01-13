package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PersonGroupTest {

    @DisplayName("연령별 할인금액 테스트")
    @Test
    void discountFarePerAgeTest() {
        int childAge = 7;
        int teenagerAge = 15;
        int adultAge = 20;

        long fare = 1350;

        assertThat(PersonGroup.discountFarePerAge(childAge,fare)).isEqualTo(500L);
        assertThat(PersonGroup.discountFarePerAge(teenagerAge,fare)).isEqualTo(200L);
        assertThat(PersonGroup.discountFarePerAge(adultAge,fare)).isEqualTo(0L);
    }
}