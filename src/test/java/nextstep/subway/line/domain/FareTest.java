package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.line.domain.AgeFarePolicy;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("요금 관련 도메인 테스트")
public class FareTest {

    @ParameterizedTest(name = "요금 생성 시, 요금이 0보다 작으면 에러가 발생한다. (fare: {0})")
    @ValueSource(ints = {-1, -4, -6})
    void createFareThrowErrorWhenFareLessThenZero(int fare) {
        // when & then
        assertThatThrownBy(() -> Fare.from(fare))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.요금은_0보다_작을_수_없음.getErrorMessage());
    }

    @DisplayName("요금에서 요금을 빼면 새로운 요금이 나온다.")
    @Test
    void subtractFare() {
        // given
        BigDecimal original = BigDecimal.valueOf(15000);
        BigDecimal target = BigDecimal.valueOf(2000);
        Fare fare = Fare.from(original);
        Fare subtractFare = Fare.from(target);

        // when
        Fare resultFare = fare.subtract(subtractFare);

        // then
        assertThat(resultFare.value()).isEqualTo(original.subtract(target));
    }

    @DisplayName("요금에서 요금을 더하면 새로운 요금이 나온다.")
    @Test
    void addFare() {
        // given
        BigDecimal original = BigDecimal.valueOf(15000);
        BigDecimal target = BigDecimal.valueOf(2000);
        Fare fare = Fare.from(original);
        Fare subtractFare = Fare.from(target);

        // when
        Fare resultFare = fare.add(subtractFare);

        // then
        assertThat(resultFare.value()).isEqualTo(original.add(target));
    }

    @ParameterizedTest(name = "{0}에 {1}을 곱하면, 소숫점에서 올림처리가 된 정수인 {2}를 반환한다.")
    @CsvSource(value = {"20:0.8:16", "25:0.3:8", "32:0.2:7"}, delimiter = ':')
    void mutiplyAndCeil(int number, double multiplyNumber, int expect) {
        // when & then
        assertThat(Fare.from(number).multiplyAndCeil(multiplyNumber)).isEqualTo(Fare.from(expect));
    }

    @ParameterizedTest(name = "{0}에 {1}을 곱하면, {2}가 반환된다.")
    @CsvSource(value = {"2000:2:4000", "1500:2:3000"}, delimiter = ':')
    void multiply(int number, int count, int expect) {
        // when & then
        assertThat(Fare.from(number).multiply(count)).isEqualTo(Fare.from(expect));
    }

    @ParameterizedTest(name = "이용거리가 {0}이면, 성인 요금 기준 {1}원, 어린이는 {2}원, 청소년은 {3}원이다. (아가는 {4}원)")
    @CsvSource(value = {"6:1250:450:720:0", "18:1450:550:880:0", "123:3050:1350:2160:0", "74:2350:1000:1600:0"}, delimiter = ':')
    void createFareOfAgeAndDistance(int distance, BigDecimal adultCharge, BigDecimal childCharge, BigDecimal teenagerCharge, BigDecimal babyCharge) {
        // when
        Fare adultFare = Fare.createFare(AgeFarePolicy.ADULT, Distance.from(distance));
        Fare childFare = Fare.createFare(AgeFarePolicy.CHILD, Distance.from(distance));
        Fare teenagerFare = Fare.createFare(AgeFarePolicy.TEENAGER, Distance.from(distance));
        Fare babyFare = Fare.createFare(AgeFarePolicy.BABY, Distance.from(distance));

        // then
        assertAll(
                () -> assertThat(adultFare.value()).isEqualTo(adultCharge),
                () -> assertThat(childFare.value()).isEqualTo(childCharge),
                () -> assertThat(teenagerFare.value()).isEqualTo(teenagerCharge),
                () -> assertThat(babyFare.value()).isEqualTo(babyCharge)
        );
    }

    @ParameterizedTest(name = "이용거리가 {0}이면, 비로그인 사용자는 {1}원을 내야 한다.")
    @CsvSource(value = {"6:1250", "18:1450", "123:3050", "74:2350"}, delimiter = ':')
    void createFareOfDistance(int distance, BigDecimal adultFare) {
        // when
        Fare fare = Fare.createFare(Distance.from(distance));

        // then
        assertThat(fare.value()).isEqualTo(adultFare);
    }
}
