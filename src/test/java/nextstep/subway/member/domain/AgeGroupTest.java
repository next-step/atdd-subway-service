package nextstep.subway.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeGroupTest {
    @DisplayName("나이별 나이 그룹 조회 테스트")
    @ParameterizedTest(name = "나이가 {0}일 때 해당하는 거리 요금 정책 {1} 확인 테스트")
    @CsvSource(value = {"10, CHILD", "30, ETC", "16, TEEN"})
    void findAgeFarePolicyByAge(int input, AgeGroup ageGroup) {
        AgeGroup ageGroupByAge = AgeGroup.findAgeGroupByAge(Age.valueOf(input));
        assertThat(ageGroupByAge).isEqualTo(ageGroup);
    }
}
