package nextstep.subway.path.domain.discount;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DiscountPolicyTest {

    @DisplayName("청소년은 350원을 공제한 금액에 20% 할인된 금액을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"0,0", "350,0", "1000,520", "3300,2360", "17580,13784"})
    void calculateKidDiscountFee(int fee, int expected) {
        int discount = new AdolescentDiscountPolicy().discount(fee);
        assertEquals(expected, discount);
    }

    @DisplayName("어린이는 350원을 공제한 금액에 50% 할인된 금액을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"0,0", "350,0", "1000,325", "3300,1475", "17580,8615"})
    void calculateAdolescentDiscountFee(int fee, int expected) {
        int discount = new KidDiscountPolicy().discount(fee);
        assertEquals(expected, discount);
    }

}
