package nextstep.subway.path.domain.fare;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DiscountAgeTest {

    @ParameterizedTest(name = "연령별 할인: [{0}]={1}")
    @CsvSource(value = {"2, 0", "7, 450", "15, 720", "40, 1250", "71, 0"})
    void getOverDistance50km(int age, int excepted) {
        // given
        Fare fare = new Fare();

        // when
        int discountFare = DiscountAge.getDiscountFare(age, fare);

        // then
        assertThat(discountFare).isEqualTo(excepted);
    }
}
