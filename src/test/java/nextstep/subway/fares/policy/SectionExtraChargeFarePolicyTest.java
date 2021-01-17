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

public class SectionExtraChargeFarePolicyTest {

    static SectionExtraChargeFarePolicy farePolicy;

    @BeforeAll
    static void setUp() {
        farePolicy = new SectionExtraChargeFarePolicy();
    }

    @DisplayName("노선별 추가 요금 정책을 확인한다")
    @Test
    void calculateFare() {
        // given
        Fare fare = new Fare();
        Path path = new Path(Collections.emptyList(), new Distance(10), 1000);
        FareContext fareContext = new FareContext(path , LoginMember.createAnonymousMember());
        // when
        farePolicy.calculateFare(fare, fareContext);

        // then
        assertThat(fare.getFare()).isEqualTo(1000);
    }
}
