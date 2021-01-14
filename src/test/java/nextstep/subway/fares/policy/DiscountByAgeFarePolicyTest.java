package nextstep.subway.fares.policy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fares.domain.Fare;
import nextstep.subway.path.domain.Distance;
import nextstep.subway.path.domain.Path;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class DiscountByAgeFarePolicyTest {
    static DiscountByAgeFarePolicy farePolicy;
    static Path path;

    @BeforeAll
    static void setUp() {
        farePolicy = new DiscountByAgeFarePolicy();
        Distance targetDistance = new Distance(10);
        path = new Path(Collections.emptyList(), targetDistance, 0);
    }

    @DisplayName("청소년의 요금 할인 정책을 확인한다")
    @Test
    void calculateFareTeenager() {
        // given
        Fare fare = new Fare();
        fare.add(1000);

        // when
        farePolicy.calculateFare(fare, path, new LoginMember(1L, "aaa@gmail.com", 15));

        // then
        assertThat(fare.getFare() * 1.0).isEqualTo((1000 - 350) * 0.8);
    }

    @DisplayName("어린이의 요금 할인 정책을 확인한다")
    @Test
    void calculateFareChildren() {
        // given
        Fare fare = new Fare();
        fare.add(1000);

        // when
        farePolicy.calculateFare(fare, path, new LoginMember(1L, "aaa@gmail.com", 6));

        // then
        assertThat(fare.getFare() * 1.0).isEqualTo((1000 - 350) * 0.5);
    }

    @DisplayName("요금 할인 정책이 적용되지 않는 연령을 확인한다")
    @Test
    void calculateFareWithOtherAge() {
        // given
        Fare fare = new Fare();
        fare.add(1000);

        // when
        farePolicy.calculateFare(fare, path, new LoginMember(1L, "aaa@gmail.com", 1));

        // then
        assertThat(fare.getFare()).isEqualTo(1000);
    }
}
