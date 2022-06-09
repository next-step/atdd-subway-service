package nextstep.subway.fare;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.fare.domain.FareSectionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FareSectionTypeTest {

    @DisplayName("10km 이내 distance 의 FareSectionType은 BASIC 이다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 9, 10})
    void findFareSectionType01(int distance) {
        // given && when
        FareSectionType fareSectionType = FareSectionType.findTypeByDistance(distance);

        // then
        assertThat(fareSectionType).isEqualTo(FareSectionType.BASIC);
    }

    @DisplayName("10km 초과 ~ 50km 까지 사이의 distance 의 FareSectionType은 SECTION_10_TO_50 이다.")
    @ParameterizedTest
    @ValueSource(ints = {11, 20, 30, 40, 50})
    void findFareSectionType02(int distance) {
        // given && when
        FareSectionType fareSectionType = FareSectionType.findTypeByDistance(distance);

        // then
        assertThat(fareSectionType).isEqualTo(FareSectionType.SECTION_10_TO_50);
    }

    @DisplayName("50km 초과한 distance 의 FareSectionType은 SECTION_OVER_50 이다.")
    @ParameterizedTest
    @ValueSource(ints = {51, 60, 100})
    void findFareSectionType03(int distance) {
        // given && when
        FareSectionType fareSectionType = FareSectionType.findTypeByDistance(distance);

        // then
        assertThat(fareSectionType).isEqualTo(FareSectionType.SECTION_OVER_50);
    }
}