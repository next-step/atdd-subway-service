package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.exception.IllegalDistanceException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceTest {

    @Test
    void 차감_테스트() {
        //given
        Distance distance = new Distance(7);

        //when
        Distance newDistance = distance.minus(new Distance(4));

        //then
        assertThat(newDistance).isEqualTo(new Distance(3));
    }

    @Test
    void 증감_테스트() {
        //given
        Distance distance = new Distance(7);

        //when
        Distance newDistance = distance.plus(new Distance(4));

        //then
        assertThat(newDistance).isEqualTo(new Distance(11));
    }

    @Test
    void 차감시_값이_0이하로_내려가면_오류() {
        //given
        Distance distance = new Distance(7);

        //when
        assertThatThrownBy(() -> distance.minus(new Distance(7)))
            .isInstanceOf(IllegalDistanceException.class)
            .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @ParameterizedTest
    @CsvSource(value = {"54:100", "59:200", "67:300", "74:300"}, delimiter = ':')
    void 장거리의_경우_8KM까지_마다_금액이_100원씩_추가된다(int distance, int additionalFare) {
        //when
        Fare fare = new Distance(distance).additionalFareByDistance();

        //then
        Assertions.assertThat(fare).isEqualTo(new Fare(additionalFare));
    }

    @ParameterizedTest
    @CsvSource(value = {"14:100", "15:100", "17:200", "21:300"}, delimiter = ':')
    void 중거리의_경우_5KM까지_마다_금액이_100원씩_추가된다(int distance, int additionalFare) {
        //when
        Fare fare = new Distance(distance).additionalFareByDistance();

        //then
        Assertions.assertThat(fare).isEqualTo(new Fare(additionalFare));
    }

    @ParameterizedTest
    @CsvSource(value = {"1:0", "9:0"}, delimiter = ':')
    void 단거리의_경우_추가요금이_없다(int distance, int additionalFare) {
        //when
        Fare fare = new Distance(distance).additionalFareByDistance();

        //then
        Assertions.assertThat(fare).isEqualTo(new Fare(additionalFare));
    }


}