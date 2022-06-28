package nextstep.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class AgeGroupTest {

    @DisplayName("나이에 따른 해당 나이 그룹이 정상 조회되어야 한다")
    @ParameterizedTest(name = "age: {0}")
    @ValueSource(ints = {6, 12, 13, 18, 19})
    void findAgeGroupByAgeTest(int value) {
        // given
        Age age = new Age(value);

        // when
        AgeGroup ageGroup = AgeGroup.getAgeGroupByAge(age);

        // then
        나이_그룹_정상_조회됨(ageGroup, value);
    }

    @DisplayName("나이 그룹에 해당하지 않는 나이로 조회하면 예외가 발생해야 한다")
    @Test
    void findAgeGroupNotValidAgeTest() {
        // given
        Age underChildAge = new Age(5);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> AgeGroup.getAgeGroupByAge(underChildAge));
    }

    @DisplayName("나이 그룹에 따른 운임 할인율이 정상 계산되어야 한다")
    @Test
    void checkDiscountCalculateTest() {
        // given
        int lineFare = 2050;

        // when
        int childLineFare = AgeGroup.CHILD.discountLineFare(lineFare);
        int teenagerLineFare = AgeGroup.TEENAGER.discountLineFare(lineFare);
        int adultLineFare = AgeGroup.ADULT.discountLineFare(lineFare);

        // then
        assertThat(childLineFare).isEqualTo(850);
        assertThat(teenagerLineFare).isEqualTo(1360);
        assertThat(adultLineFare).isEqualTo(2050);
    }

    void 나이_그룹_정상_조회됨(AgeGroup source, int originalAge) {
        if (originalAge >= AgeGroup.MINIMUM_CHILD_AGE && originalAge < AgeGroup.MINIMUM_TEENAGER_AGE) {
            assertThat(source.name()).isEqualTo(AgeGroup.CHILD.name());
            return;
        }
        if (originalAge >= AgeGroup.MINIMUM_TEENAGER_AGE && originalAge < AgeGroup.MINIMUM_ADULT_AGE) {
            assertThat(source.name()).isEqualTo(AgeGroup.TEENAGER.name());
            return;
        }
        if (originalAge >= AgeGroup.MINIMUM_ADULT_AGE) {
            assertThat(source.name()).isEqualTo(AgeGroup.ADULT.name());
            return;
        }
        fail("나이 그룹을 찾을 수 없습니다.");
    }
}
