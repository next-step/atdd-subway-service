package nextstep.subway.path.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AgeDiscountPolicyTest {

    @ParameterizedTest(name = "청소년일 경우 운임에서 350원을 공제한 금액의 20% 할인 한다. {0} 거리는 {1} 요금")
    @CsvSource({"1250,13,720", "1350,14,800", "1450,15,880", "1550,16,960", "1650,17,1040", "1750,18,1120"})
    void discountYouth(int fare, int age, int expectedFare) {
        // when
        final Fare discountedFare = AgeDiscountPolicy.discount(Fare.of(fare), age);
        // then
        assertTrue(discountedFare.match(expectedFare));
    }

    @ParameterizedTest(name = "어린이일 경우 운임에서 350원을 공제한 금액의 50% 할인 한다. {0} 거리는 {1} 요금")
    @CsvSource({"1250,6,450", "1350,7,500", "1450,8,550", "1550,9,600", "1650,10,650", "1750,11,700", "1850,12,750"})
    void discountChild(int fare, int age, int expectedFare) {
        // when
        final Fare discountedFare = AgeDiscountPolicy.discount(Fare.of(fare), age);
        // then
        assertTrue(discountedFare.match(expectedFare));
    }
}