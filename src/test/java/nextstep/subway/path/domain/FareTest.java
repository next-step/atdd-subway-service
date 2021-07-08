package nextstep.subway.path.domain;

import nextstep.subway.enums.SubwayFarePolicy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @ParameterizedTest
    @CsvSource(value = {
            "10:0:1250",
            "11:0:1350",
            "14:0:1350",
            "16:0:1450",
            "11:900:2250",
            "50:0:2050",
            "51:0:2150",
            "58:0:2150",
            "59:0:2250",
    }, delimiter = ':')
    void 이동거리에_지하철_요금_계산_성인(int totalDistance, int maxExtraFare, int expected) {
        Fare fare = new Fare(maxExtraFare);
        int subwayFare = fare.calcSubwayFare(totalDistance, SubwayFarePolicy.ADULT);
        assertThat(subwayFare).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "10:0:1000",
            "11:0:1080",
            "14:0:1080",
            "16:0:1160",
            "11:900:1800",
            "50:0:1640",
            "51:0:1720",
            "58:0:1720",
            "59:0:1800",
    }, delimiter = ':')
    void 이동거리에_지하철_요금_계산_청소년(int totalDistance, int maxExtraFare, int expected) {
        Fare fare = new Fare(maxExtraFare);
        int subwayFare = fare.calcSubwayFare(totalDistance, SubwayFarePolicy.TEENAGER);
        assertThat(subwayFare).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "10:0:625",
            "11:0:675",
            "14:0:675",
            "16:0:725",
            "11:900:1125",
            "50:0:1025",
            "51:0:1075",
            "58:0:1075",
            "59:0:1125",
    }, delimiter = ':')
    void 이동거리에_지하철_요금_계산_어린이(int totalDistance, int maxExtraFare, int expected) {
        Fare fare = new Fare(maxExtraFare);
        int subwayFare = fare.calcSubwayFare(totalDistance, SubwayFarePolicy.CHILD);
        assertThat(subwayFare).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "10:0:0",
            "11:0:0",
            "14:0:0",
            "16:0:0",
            "11:900:0",
            "50:0:0",
            "51:0:0",
            "58:0:0",
            "59:0:0",
    }, delimiter = ':')
    void 이동거리에_지하철_요금_계산_6세미만(int totalDistance, int maxExtraFare, int expected) {
        Fare fare = new Fare(maxExtraFare);
        int subwayFare = fare.calcSubwayFare(totalDistance, SubwayFarePolicy.BABY);
        assertThat(subwayFare).isEqualTo(expected);
    }
}
