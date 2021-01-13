package nextstep.subway.fares.policy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fares.domain.Fare;
import nextstep.subway.path.domain.Distance;
import nextstep.subway.path.domain.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 정책 테스트")
public class FarePoliciesTest {

    @DisplayName("거리가 10km 이내의 운임을 확인한다")
    @Test
    void calculateFareWithLessThan10km() {
        // given
        FarePolicies farePolicies = new FarePolicies();
        Path path = new Path(Collections.emptyList(), new Distance(10), 0);
        // when
        Fare fare = farePolicies.calculateFare(path, LoginMember.createAnonymousMember());
        // then
        assertThat(fare.getFare()).isEqualTo(1250);
    }

    @DisplayName("거리가 10km~50km 이내의 운임을 확인한다")
    @Test
    void calculateFareWithOver10kmLessThan50km() {
        // given
        FarePolicies farePolicies = new FarePolicies();
        Path path = new Path(Collections.emptyList(), new Distance(15), 0);
        // when
        Fare fare = farePolicies.calculateFare(path, LoginMember.createAnonymousMember());
        // then
        assertThat(fare.getFare()).isEqualTo(1350);
    }

    @DisplayName("거리가 50km 초과인 운임을 확인한다")
    @Test
    void calculateFareWithOver50km() {
        // given
        FarePolicies farePolicies = new FarePolicies();
        Path path = new Path(Collections.emptyList(), new Distance(65), 0);
        // when
        Fare fare = farePolicies.calculateFare(path, LoginMember.createAnonymousMember());
        // then
        assertThat(fare.getFare()).isEqualTo(2150);
    }
}
