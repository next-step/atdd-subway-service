package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AgeFarePolicyTest {
    public static final int TEENAGER_AGE = 15;
    public static final int CHILDREN_AGE = 9;
    public static final int ALL_AGE = 30;
    public static final int FREE_AGE = 70;

    @Test
    @DisplayName("일반요금_나이대이면_일반_요금정책_가져온다")
    void findAgeFarePolicyForAll() {
        FarePolicy farePolicy = AgeFarePolicy.findAgeFarePolicy(ALL_AGE);
        assertThat(farePolicy).isInstanceOf(AllFarePolicy.class);
    }

    @Test
    @DisplayName("청소년_나이대이면_청소년_요금정책_가져온다")
    void findAgeFarePolicyForTeenager() {
        FarePolicy farePolicy = AgeFarePolicy.findAgeFarePolicy(TEENAGER_AGE);
        assertThat(farePolicy).isInstanceOf(TeenagerFarePolicy.class);
    }

    @Test
    @DisplayName("아동_나이대이면_아동_요금정책_가져온다")
    void findAgeFarePolicyForChildren() {
        FarePolicy farePolicy = AgeFarePolicy.findAgeFarePolicy(CHILDREN_AGE);
        assertThat(farePolicy).isInstanceOf(ChildrenFarePolicy.class);
    }

    @Test
    @DisplayName("무료대상_나이대이면_무료대상_요금정책_가져온다")
    void findAgeFarePolicyForFree() {
        FarePolicy farePolicy = AgeFarePolicy.findAgeFarePolicy(FREE_AGE);
        assertThat(farePolicy).isInstanceOf(FreeFarePolicy.class);
    }
}