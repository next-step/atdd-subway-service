package nextstep.subway.auth.domain;

import static nextstep.subway.auth.domain.AgeGroup.*;
import static nextstep.subway.auth.domain.LoginMember.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AgeGroupTest {

    @DisplayName("나이별 AgeGroup 구하기")
    @Test
    void ageGroup1() {
        assertThat(AgeGroup.calculate(6)).isEqualTo(CHILD);
        assertThat(AgeGroup.calculate(12)).isEqualTo(CHILD);
        assertThat(AgeGroup.calculate(13)).isEqualTo(YOUTH);
        assertThat(AgeGroup.calculate(18)).isEqualTo(YOUTH);

        assertThat(AgeGroup.calculate(19)).isEqualTo(NO_SALE_AGE);
        assertThat(AgeGroup.calculate(5)).isEqualTo(NO_SALE_AGE);
    }

    @DisplayName("게스트 유저는 NO_SALE_AGE 에 속한다")
    @Test
    void ageGroup2() {
        assertThat(GUEST.getAgeGroup()).isEqualTo(NO_SALE_AGE);
    }
}
