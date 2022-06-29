package nextstep.subway.path.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AgeGroupTest {
    @Test
    void adultDontReceiveDiscount() {
        // given
        int fare = 1250;
        int age = 30;

        // when
        int i = AgeGroup.findAgeGroup(age).discountFare(fare);

        // then
        assertThat(i).isEqualTo(1250);
    }

    @Test
    void discountFareForTeenager() {
        // given
        int fare = 1450;
        int age = 17;

        // when
        int i = AgeGroup.findAgeGroup(age).discountFare(fare);

        // then
        assertThat(i).isEqualTo(880);
    }

    @Test
    void discountFareForChild() {
        // given
        int fare = 1650;
        int age = 10;

        // when
        int i = AgeGroup.findAgeGroup(age).discountFare(fare);

        // then
        assertThat(i).isEqualTo(650);
    }
}