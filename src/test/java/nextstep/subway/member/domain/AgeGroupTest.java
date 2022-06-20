package nextstep.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class AgeGroupTest {

    @DisplayName("나이에 따른 해당 나이 그룹이 정상 조회되어야 한다")
    @Test
    void findAgeGroupByAgeTest() {
        // given
        int childMinimumAge = 6;
        int childMaximumAge = 12;
        int teenagerMinimumAge = 13;
        int teenagerMaximumAge = 18;
        int adultMinimumAge = 19;

        // when
        AgeGroup childMinimumAgeGroup = AgeGroup.getAgeGroupByAge(childMinimumAge);
        AgeGroup childMaximumAgeGroup = AgeGroup.getAgeGroupByAge(childMaximumAge);
        AgeGroup teenagerMinimumAgeGroup = AgeGroup.getAgeGroupByAge(teenagerMinimumAge);
        AgeGroup teenagerMaximumAgeGroup = AgeGroup.getAgeGroupByAge(teenagerMaximumAge);
        AgeGroup adultMinimumAgeGroup = AgeGroup.getAgeGroupByAge(adultMinimumAge);

        // then
        assertThat(childMinimumAgeGroup).isEqualTo(AgeGroup.CHILD);
        assertThat(childMaximumAgeGroup).isEqualTo(AgeGroup.CHILD);
        assertThat(teenagerMinimumAgeGroup).isEqualTo(AgeGroup.TEENAGER);
        assertThat(teenagerMaximumAgeGroup).isEqualTo(AgeGroup.TEENAGER);
        assertThat(adultMinimumAgeGroup).isEqualTo(AgeGroup.ADULT);
    }

    @DisplayName("나이 그룹에 해당하지 않는 나이로 조회하면 예외가 발생해야 한다")
    @Test
    void findAgeGroupNotValidAgeTest() {
        // given
        int underChildAge = 5;
        int minusAge = -1;

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> AgeGroup.getAgeGroupByAge(underChildAge));
        assertThatIllegalArgumentException().isThrownBy(() -> AgeGroup.getAgeGroupByAge(minusAge));
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
}
