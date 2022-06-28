package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {
    @Test
    @DisplayName("전체 금액 구하기")
    void getTotalFare() {
        // when              // 1250  + 500 + 60km 초과 요금(1000)
        final Fare fare = Fare.of(LoginMember.ofNonMember(), 500, 60);
        // then
        assertThat(fare.getValue()).isEqualTo(2750);
    }
}