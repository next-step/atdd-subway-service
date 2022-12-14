package nextstep.subway.fare.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FareTest {

    @Test
    void 요금_합산_테스트() {
        //when
        Fare addedFare = Fare.DEFAULT_FARE.plus(new Fare(300));

        //then
        Assertions.assertThat(addedFare).isEqualTo(new Fare(1550));
    }

    @Test
    void 요금_감산_테스트() {
        //when
        Fare subtractedFare = Fare.DEFAULT_FARE.minus(new Fare(300));

        //then
        Assertions.assertThat(subtractedFare).isEqualTo(new Fare(950));
    }

    @Test
    void 요금_할인_테스트() {
        //when
        Fare discountedFare = Fare.DEFAULT_FARE.minus(new Fare(350)).discount(20);

        //then
        Assertions.assertThat(discountedFare).isEqualTo(new Fare(720));
    }
}