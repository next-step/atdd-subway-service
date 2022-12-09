package nextstep.subway.path.domain.discount;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("연령별 할인")
class AgeDiscountTest {

    @DisplayName("청소년 할인 / 운임에서 350원을 공제한 금액의 20%할인")
    @ParameterizedTest
    @CsvSource(value = {"2350:17:1600", "2350:13:1600", "2350:18:1600"}, delimiter = ':')
    void discount_teenager(int fare, int age, int result) {
        assertThat(AgeDiscount.create(fare, age).discount()).isEqualTo(result);
    }
}
