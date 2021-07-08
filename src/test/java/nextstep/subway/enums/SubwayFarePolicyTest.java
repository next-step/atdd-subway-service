package nextstep.subway.enums;

import nextstep.subway.exception.SubwayPatchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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


}
