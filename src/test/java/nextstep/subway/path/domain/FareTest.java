package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import nextstep.subway.auth.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @DisplayName("거리 및 노선요금에 따라 알맞은 금액을 리턴한다.")
    @Test
    void calculate_over_fare() {
        int distance = 60;
        int lineFare = 100;
        Fare fare = Fare.of(distance, lineFare, new LoginMember());
        assertThat(fare.getFare()).isEqualTo(2350);
    }
}