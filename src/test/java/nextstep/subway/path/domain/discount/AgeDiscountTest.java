package nextstep.subway.path.domain.discount;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.subway.path.domain.discount.AgeDiscount.create;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("연령별 할인")
class AgeDiscountTest {

    @DisplayName("청소년 할인 / 운임에서 350원을 공제한 금액의 20%할인")
    @ParameterizedTest
    @CsvSource(value = {"2350:17:1600", "2350:13:1600", "2350:18:1600"}, delimiter = ':')
    void discount_teenager(int fare, int age, int result) {
        assertThat(create(fare, age).discount()).isEqualTo(result);
    }

    @DisplayName("어린이 할인 / 운임에서 350원을 공제한 금액의 50%할인")
    @ParameterizedTest
    @CsvSource(value = {"2350:6:1000", "2350:12:1000"}, delimiter = ':')
    void discount_children(int fare, int age, int result) {
        assertThat(create(fare, age).discount()).isEqualTo(result);
    }

    @DisplayName("할인 없음")
    @ParameterizedTest
    @CsvSource(value = {"2350:19:2350", "2350:20:2350"}, delimiter = ':')
    void discount_default(int fare, int age, int result) {
        assertThat(create(fare, age).discount()).isEqualTo(result);
    }
}
