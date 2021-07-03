package nextstep.subway.path.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.fare.domain.Fare;

@DisplayName("요금 객체 단위 테스트")
class FareTest {
    @Test
    @DisplayName("연령대별 요금을 검증한다.")
    void adultFare() {
        Fare fare = new Fare(2050);
        Assertions.assertThat(fare.adultFare()).isEqualTo(2050);
        Assertions.assertThat(fare.teenagerFare()).isEqualTo(1360);
        Assertions.assertThat(fare.childFare()).isEqualTo(850);
    }
}
