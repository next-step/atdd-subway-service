package nextstep.subway.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AgeTypeTest {

    @DisplayName("0~5 세인 경우 영유아로 판단한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 3, 5})
    void testInfantType(int age) {
        assertThat(AgeType.checkAgeType(age)).isEqualTo(AgeType.INFANT);
    }

    @DisplayName("6~12 세인 경우 영유아로 판단한다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 9, 12})
    void testChildType(int age) {
        assertThat(AgeType.checkAgeType(age)).isEqualTo(AgeType.CHILD);
    }

    @DisplayName("13~18 세인 경우 영유아로 판단한다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 15, 18})
    void testYouthType(int age) {
        assertThat(AgeType.checkAgeType(age)).isEqualTo(AgeType.YOUTH);
    }

    @DisplayName("19세 이상인 경우 성인으로 판단한다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 50, 100})
    void checkAdultType(int age) {
        assertThat(AgeType.checkAgeType(age)).isEqualTo(AgeType.ADULT);
    }
}