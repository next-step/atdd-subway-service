package nextstep.subway.fare.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @DisplayName("이동 거리에 따른 추가 금액을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"10:1250", "11:1350", "15:1350", "16:1450", "49:2050", "50:2050", "51:2150", "58:2150", "59:2250"},
            delimiter = ':')
    void 거리_비례_추가_금액_계산(int distance, int totalFare) {
        // given
        Fare baseFare = Fare.of(DistanceExtraFare.BASE_FARE);

        // when
        Fare result = baseFare.addExtraOf(distance);

        // then
        assertThat(result).isEqualTo(Fare.of(totalFare));
    }
}
