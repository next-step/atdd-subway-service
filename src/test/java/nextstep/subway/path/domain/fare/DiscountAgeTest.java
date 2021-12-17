package nextstep.subway.path.domain.fare;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DiscountAgeTest {

    @ParameterizedTest(name = "연령별 할인: [{0}]={1}")
    @CsvSource(value = {"2, 0.0f", "7, 0.5f", "15, 0.8f", "40, 1.0f", "71, 0.0f"})
    void getOverDistance50km(int age, float excepted) {
        // given // when
        DiscountAge discountAge = DiscountAge.findBy(age);

        // then
        assertThat(discountAge.getRate()).isEqualTo(excepted);
    }
}
