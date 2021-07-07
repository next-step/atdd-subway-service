package nextstep.subway.enums;

import nextstep.subway.exception.SubwayPatchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubwayFarePolicyTest {


    @ParameterizedTest
    @ValueSource(ints = {19, 20, 30})
    void 회원_나이에_따라_요금_정책_가져오기_성인(int age) {
        SubwayFarePolicy subwayFarePolicy = SubwayFarePolicy.findFarePolicy(age);

        assertThat(subwayFarePolicy).isEqualTo(SubwayFarePolicy.ADULT);
    }

    @ParameterizedTest
    @ValueSource(ints = {13, 15, 18})
    void 회원_나이에_따라_요금_정책_가져오기_청소년(int age) {
        SubwayFarePolicy subwayFarePolicy = SubwayFarePolicy.findFarePolicy(age);
        assertThat(subwayFarePolicy).isEqualTo(SubwayFarePolicy.TEENAGER);
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 7, 12})
    void 회원_나이에_따라_요금_정책_가져오기_어린이(int age) {
        SubwayFarePolicy subwayFarePolicy = SubwayFarePolicy.findFarePolicy(age);
        assertThat(subwayFarePolicy).isEqualTo(SubwayFarePolicy.CHILD);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5})
    void 회원_나이에_따라_요금_정책_가져오기_6세_미만(int age) {
        SubwayFarePolicy subwayFarePolicy = SubwayFarePolicy.findFarePolicy(age);
        assertThat(subwayFarePolicy).isEqualTo(SubwayFarePolicy.BABY);
    }

    @Test
    void 회원_나이가_음수인_경우_에러_발생() {
        int age = -1;
        assertThatThrownBy(() -> SubwayFarePolicy.findFarePolicy(age)).isInstanceOf(SubwayPatchException.class);
    }

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
        int subwayFare = SubwayFarePolicy.ADULT.calcSubwayFare(totalDistance, maxExtraFare);
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
    void 이동거리에_지하철_요금_계산_청소년() {
        int subwayFare = SubwayFarePolicy.TEENAGER.calcSubwayFare(10, 0);
        assertThat(subwayFare).isEqualTo(1000);
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
        int subwayFare = SubwayFarePolicy.CHILD.calcSubwayFare(totalDistance, maxExtraFare);
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
        int subwayFare = SubwayFarePolicy.BABY.calcSubwayFare(totalDistance, maxExtraFare);
        assertThat(subwayFare).isEqualTo(expected);
    }
}
