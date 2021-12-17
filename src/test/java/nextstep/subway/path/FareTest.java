package nextstep.subway.path;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.path.domain.AgeDiscount;
import nextstep.subway.path.domain.overfare.DefaultOverFare;
import nextstep.subway.path.domain.overfare.OverFare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @DisplayName("거리 별 요금 추가(5km 마다 100원추가)")
    @Test
    void calculateOverFareByFiveKM() {

        //given
        OverFare defaultOverFare = new DefaultOverFare();

        int expectedFare = 2050;

        //when
        int actualFare = defaultOverFare.calculate(50);

        //then
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("거리 별 요금 추가(8km 마다 100원추가)")
    @Test
    void calculateOverFareByEightKM() {

        //given
        OverFare defaultOverFare = new DefaultOverFare();

        int expectedFare = 2350;

        //when
        int actualFare = defaultOverFare.calculate(70);

        //then
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("할인되지 않은 금액 구하기")
    @Test
    void notDiscountFare() {

        //given
        int fare = 2150;

        //when
        Fare discountFare = AgeDiscount.discount(19, new Fare(fare));

        //then
        assertThat(discountFare.getValue()).isEqualTo(2150);

    }


    @DisplayName("청소년 할인된 금액 구하기")
    @Test
    void discountYouthFare() {

        int fare = 2150;

        //when
        Fare discountFare = AgeDiscount.discount(18, new Fare(fare));

        //then
        assertThat(discountFare.getValue()).isEqualTo(1440);

    }

    @DisplayName("어린이 할인된 금액 구하기")
    @Test
    void discountChild() {

        int fare = 2150;

        //when
        Fare discountFare = AgeDiscount.discount(12, new Fare(fare));

        //then
        assertThat(discountFare.getValue()).isEqualTo(900);
    }
}
