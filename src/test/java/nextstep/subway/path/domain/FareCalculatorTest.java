package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FarexCalculatorTest {
    @Test
    @DisplayName("라인추가요금 1000 + 60키로 이동시 성인기본요금(1250) + 50키로이내 추가요금(800) + 50키로초과 추가요금(200) = 3250")
    void extraFareWithLineExtraFare() {
        Integer fare = FareCalculator.calculateFare(1000, 60, getMember(20));
        assertThat(fare).isEqualTo(3250);
    }

    private LoginMember getMember(int age) {
        return new LoginMember(1L, "mj@naver.com", age);
    }
}