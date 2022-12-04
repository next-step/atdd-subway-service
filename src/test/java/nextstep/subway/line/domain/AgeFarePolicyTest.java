package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.AgeFarePolicy.findAgeFarePolicy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.member.domain.Age;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("연령별 요금 정책 도메인 테스트")
class AgeFarePolicyTest {

    @ParameterizedTest(name = "나이가 {0}살이면, 성인 요금제를 낸다.")
    @ValueSource(ints = {19, 20, 35, 40, 56})
    void isAdult(int age) {
        // when
        AgeFarePolicy ageFarePolicy = findAgeFarePolicy(Age.from(age));

        // then
        assertThat(ageFarePolicy).isEqualTo(AgeFarePolicy.ADULT);
    }

    @ParameterizedTest(name = "나이가 {0}살이면, 어린이 요금제를 낸다.")
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void isChild(int age) {
        // when
        AgeFarePolicy ageFarePolicy = findAgeFarePolicy(Age.from(age));

        // then
        assertThat(ageFarePolicy).isEqualTo(AgeFarePolicy.CHILD);
    }

    @ParameterizedTest(name = "나이가 {0}살이면, 청소년 요금제를 낸다.")
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void isTeenager(int age) {
        // when
        AgeFarePolicy ageFarePolicy = findAgeFarePolicy(Age.from(age));

        // then
        assertThat(ageFarePolicy).isEqualTo(AgeFarePolicy.TEENAGER);
    }

    @ParameterizedTest(name = "나이가 {0}살이면, 아기이다.")
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void isBaby(int age) {
        // when
        AgeFarePolicy ageFarePolicy = findAgeFarePolicy(Age.from(age));

        // then
        assertThat(ageFarePolicy).isEqualTo(AgeFarePolicy.BABY);
    }

    @ParameterizedTest(name = "성인 요금제 기준 {0}원이면, 어린이는 {1}원을 낸다.")
    @CsvSource(value = {"2450:1050", "3650:1650"}, delimiter = ':')
    void isChildFare(int adultFare, int childFare) {
        // when & then
        assertThat(AgeFarePolicy.CHILD.calculateFare(Fare.from(adultFare))).isEqualTo(Fare.from(childFare));
    }

    @ParameterizedTest(name = "성인 요금제 기준 {0}원이면, 청소년은 {1}원을 낸다.")
    @CsvSource(value = {"3150:2240", "1950:1280"}, delimiter = ':')
    void isTeenagerFare(int adultFare, int teenagerFare) {
        // when & then
        assertThat(AgeFarePolicy.TEENAGER.calculateFare(Fare.from(adultFare))).isEqualTo(Fare.from(teenagerFare));
    }

    @DisplayName("아기는 0원을 낸다.")
    @Test
    void isBabyFare() {
        // when & then
        assertThat(AgeFarePolicy.BABY.calculateFare(Fare.from(1000000))).isEqualTo(Fare.from(0));
    }

    @DisplayName("성인이 아닐 경우 참을 반환한다.")
    @Test
    void isNotAdult() {
        // when & then
        assertAll(
                () -> assertThat(AgeFarePolicy.BABY.isNotAdult()).isTrue(),
                () -> assertThat(AgeFarePolicy.CHILD.isNotAdult()).isTrue(),
                () -> assertThat(AgeFarePolicy.TEENAGER.isNotAdult()).isTrue()
        );
    }
}
