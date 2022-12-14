package nextstep.subway.fare.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceFarePolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"54:100", "59:200", "67:300", "74:300"}, delimiter = ':')
    void 장거리의_경우_8KM까지_마다_금액이_100원씩_추가된다(int distance, int additionalFare) {
        //when
        Fare fare = DistanceFarePolicy.from(distance).apply(distance);

        //then
        Assertions.assertThat(fare).isEqualTo(new Fare(additionalFare));
    }

    @ParameterizedTest
    @CsvSource(value = {"14:100", "15:100", "17:200", "21:300"}, delimiter = ':')
    void 중거리의_경우_5KM까지_마다_금액이_100원씩_추가된다(int distance, int additionalFare) {
        //when
        Fare fare = DistanceFarePolicy.from(distance).apply(distance);

        //then
        Assertions.assertThat(fare).isEqualTo(new Fare(additionalFare));
    }

    @ParameterizedTest
    @CsvSource(value = {"1:0", "9:0"}, delimiter = ':')
    void 단거리의_경우_추가요금이_없다(int distance, int additionalFare) {
        //when
        Fare fare = DistanceFarePolicy.from(distance).apply(distance);

        //then
        Assertions.assertThat(fare).isEqualTo(new Fare(additionalFare));
    }
}