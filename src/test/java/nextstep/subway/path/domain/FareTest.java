package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Surcharge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @DisplayName("10km까지 요금")
    @Test
    void to10() {
        // given, when
        Fare fare = new Fare(new Surcharge(0), new Distance(10));

        // then
        assertThat(fare.getValue()).isEqualTo(1250);
    }

    @DisplayName("10km까지 요금 + 추가 요금")
    @Test
    void to10WithSurcharge() {
        // given, when
        Fare fare = new Fare(new Surcharge(500), new Distance(10));

        // then
        assertThat(fare.getValue()).isEqualTo(1750);
    }

    @DisplayName("15km까지 요금")
    @Test
    void to15() {
        // given, when
        Fare fare = new Fare(new Surcharge(0), new Distance(15));

        // then
        assertThat(fare.getValue()).isEqualTo(1350);
    }

    @DisplayName("15km까지 요금 + 추가 요금")
    @Test
    void to15WithSurcharge() {
        // given, when
        Fare fare = new Fare(new Surcharge(1000), new Distance(15));

        // then
        assertThat(fare.getValue()).isEqualTo(2350);
    }

    @DisplayName("20km까지 요금")
    @Test
    void to20() {
        // given, when
        Fare fare = new Fare(new Surcharge(0), new Distance(20));

        // then
        assertThat(fare.getValue()).isEqualTo(1450);
    }

    @DisplayName("50km까지 요금")
    @Test
    void to50() {
        // given, when
        Fare fare = new Fare(new Surcharge(0), new Distance(50));

        // then
        assertThat(fare.getValue()).isEqualTo(2050);
    }

    @DisplayName("66km까지 요금")
    @Test
    void to66() {
        // given, when
        Fare fare = new Fare(new Surcharge(0), new Distance(66));

        // then
        assertThat(fare.getValue()).isEqualTo(2250);
    }

    @DisplayName("82km까지 요금")
    @Test
    void to82() {
        // given, when
        Fare fare = new Fare(new Surcharge(0), new Distance(82));

        // then
        assertThat(fare.getValue()).isEqualTo(2450);
    }

    @DisplayName("178km까지 요금")
    @Test
    void to178() {
        // given, when
        Fare fare = new Fare(new Surcharge(0), new Distance(178));

        // then
        assertThat(fare.getValue()).isEqualTo(3650);
    }

    @DisplayName("178km까지 요금 + 추가 요금")
    @Test
    void to178WithSurcharge() {
        // given, when
        Fare fare = new Fare(new Surcharge(10000), new Distance(178));

        // then
        assertThat(fare.getValue()).isEqualTo(13650);
    }
}
