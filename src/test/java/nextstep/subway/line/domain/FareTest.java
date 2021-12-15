package nextstep.subway.line.domain;

import nextstep.subway.exception.BadRequestException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("요금 관련 기능")
class FareTest {

    @Test
    void 요금_생성() {
        // given - when
        Fare fare = Fare.from(1000);

        // then
        Assertions.assertThat(fare).isEqualTo(Fare.from(1000));
    }

    @Test
    void 요금을_더한다() {
        // given
        Fare fare = Fare.from(1000);
        Fare otherFare = Fare.from(500);
        // when
        fare = fare.plus(otherFare);

        Assertions.assertThat(fare).isEqualTo(Fare.from(1500));
    }

    @Test
    void 요금을_뺀다() {
        // given
        Fare fare = Fare.from(1000);
        Fare otherFare = Fare.from(1000);
        // when
        fare = fare.minus(otherFare);

        Assertions.assertThat(fare).isEqualTo(Fare.from(0));
    }

    @ParameterizedTest
    @ValueSource(ints = {500, 999})
    void 요금은_음수가_될_수_없다(int amount) {
        // given
        Fare fare = Fare.from(amount);
        Fare otherFare = Fare.from(1000);

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> fare.minus(otherFare);

        // then
        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(throwingCallable)
                .withMessage("요금은 1 이상의 값으로 입력해주세요.");
    }
}
