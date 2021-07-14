package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistancePolicyTest {

    @DisplayName("추가운임이 발생하는 거리인지 확인한다.")
    @ParameterizedTest
    @CsvSource({"2, true", "5, true", "10, false", "15, false"})
    void isNotAddedFareDistance_test(int distance, boolean expected) {
        //when
        boolean isAddedFareDistance = DistancePolicy.isNotAddedFareDistance(distance);

        //then
        assertThat(isAddedFareDistance).isEqualTo(expected);
    }

    @DisplayName("첫번째 추가운임이 발생하는 10km~50km구간인지 확인한다.")
    @ParameterizedTest
    @CsvSource({"2, false", "10, true", "20, true", "50, false", "70, false"})
    void isFirstAddedFareDistance_test(int distance, boolean expected) {
        //when
        boolean isFirstAddedFareDistance = DistancePolicy.isFirstAddedFareDistance(distance);

        //then
        assertThat(isFirstAddedFareDistance).isEqualTo(expected);
    }
}
