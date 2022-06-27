package nextstep.subway.path.domain;

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
    @DisplayName("청소년_할인_금액_계산")
    void calculateForTeenager() {
        int fare = AgeFarePolicy.calculate(2000, TEENAGER_AGE);
        assertThat(fare).isEqualTo(AgeFarePolicy.TEENAGER.getOperator().apply(2000));
    }

    @Test
    @DisplayName("아동_할인_금액_계산")
    void calculateForChildren() {
        int fare = AgeFarePolicy.calculate(2000, CHILDREN_AGE);
        assertThat(fare).isEqualTo(AgeFarePolicy.CHILDREN.getOperator().apply(2000));
    }

    @Test
    @DisplayName("무료_대상자_할인_금액_계산")
    void calculateForFree() {
        int fare = AgeFarePolicy.calculate(2000, FREE_AGE);
        assertThat(fare).isEqualTo(AgeFarePolicy.FREE.getOperator().apply(2000));
    }

    @Test
    @DisplayName("일반요금_금액_계산")
    void calculateForAll() {
        int fare = AgeFarePolicy.calculate(2000, ALL_AGE);
        assertThat(fare).isEqualTo(AgeFarePolicy.ALL.getOperator().apply(2000));
    }
}