package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.fare.discount.AgeDiscount;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class RequireCalculatedFareTest {

    @DisplayName("거리에 맞는 요금을 반환한다.")
    @CsvSource(value = {"10:1250", "11:1350", "20:1550", "49:2050", "51:2150", "60:2250", "100:2750"}, delimiter = ':')
    @ParameterizedTest
    void requireFare(int givenDistance, int expectFare) {
        // given
        Station givenStation = Station.of(1L, "교대역");
        Path givenPath = new Path(Arrays.asList(givenStation), givenDistance, 0);

        // when & then
        assertThat(RequireFare.getRequireFare(givenPath)).isEqualTo(expectFare);
    }

    @DisplayName("추가 요금을 더한 만큼의 요금이 반환된다.")
    @CsvSource(value = {"400:1650", "900:2150", "0:1250"}, delimiter = ':')
    @ParameterizedTest
    void additionalFare(int givenAdditionalFare, int expectFare) {
        // given
        Station givenStation = Station.of(1L, "교대역");
        Path givenPath = new Path(Arrays.asList(givenStation), 1, givenAdditionalFare);

        // when & then
        assertThat(RequireFare.getRequireFare(givenPath)).isEqualTo(expectFare);
    }

    @DisplayName("연령대 별로 할인된 만큼의 요금이 반환된다.")
    @CsvSource(value = {"TODDLER:0", "CHILDREN:450", "TEENAGER:720", "ADULT:1250"}, delimiter = ':')
    @ParameterizedTest
    void additionalFare(AgeDiscount givenAgeDiscount, int expectFare) {
        // given
        Station givenStation = Station.of(1L, "교대역");
        Path givenPath = new Path(Arrays.asList(givenStation), 1, 0);

        // when & then
        assertThat(RequireFare.getRequireFareWithDiscount(givenPath, givenAgeDiscount)).isEqualTo(expectFare);
    }
}
