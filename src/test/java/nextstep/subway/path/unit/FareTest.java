package nextstep.subway.path.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Discount;
import nextstep.subway.path.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest {
    
    @DisplayName("요금을 조회할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"5:1250", "20:1450", "60:2150"}, delimiter = ':')
    void createFare_test(int distanceInput, int distancePriceInput) {
        // given
        Distance distance = Distance.from(distanceInput);
        int surCharge = 0;

        // when
        Fare fare = Fare.of(distance, surCharge);
    
        // then
        assertEquals(distancePriceInput, fare.value());
    }

    @DisplayName("할인 요금을 조회할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"5,10,450,", "20,10,550", "60,10,900", "5,15,720,", "20,15,880", "60,15,1440"}, delimiter = ',')
    void createFare_discount_test(int distanceInput, int ageInput, int distancePriceInput) {
        // given
        Distance distance = Distance.from(distanceInput);
        int surCharge = 0;
        Discount discount = Discount.getDiscountByAge(ageInput);

        // when
        Fare fare = Fare.ofDiscount(distance, surCharge, discount);

        // then
        assertEquals(distancePriceInput, fare.value());
    }


}
