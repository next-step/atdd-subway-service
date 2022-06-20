package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgeFarePolicyTest {

    private AgeFarePolicy ageFarePolicy;
    private Fare originFare;

    private int childAge;
    private int teenagerAge;
    private int normalAge;

    @BeforeEach
    void setUp() {
        ageFarePolicy = new AgeFarePolicy();
        originFare = Fare.from(1350);
        childAge = 8;
        teenagerAge = 16;
        normalAge = 25;
    }

    @Test
    void 어린이_요금_할인() {
        assertThat(ageFarePolicy.discount(originFare, childAge)).isEqualTo(Fare.from(850));
    }

    @Test
    void 청소년_요금_할인() {
        assertThat(ageFarePolicy.discount(originFare, teenagerAge)).isEqualTo(Fare.from(1150));
    }

    @Test
    void 일반_요금_할인() {
        assertThat(ageFarePolicy.discount(originFare, normalAge)).isEqualTo(originFare);
    }
}