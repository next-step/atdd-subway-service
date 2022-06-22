package nextstep.subway.path.domain;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AgeFarePolicyTest {
    private AgeFarePolicy childPolicy;
    private AgeFarePolicy youthPolicy;
    private AgeFarePolicy basicPolicy;

    @DisplayName("연령별 요금정책을 조회한다.")
    @Test
    void findByAge() {
        //when
        연령별_요금정책_조회();

        //then
        assertThat(childPolicy.name()).isEqualTo("CHILD");
        assertThat(youthPolicy.name()).isEqualTo("YOUTH");
        assertThat(basicPolicy.name()).isEqualTo("BASIC");
    }

    @DisplayName("연령별 요금정책에 따른 할인을 적용한다.")
    @Test
    void calculate() {
        //given
        연령별_요금정책_조회();
        int 요금 = 1250;

        //when
        int 어린이_요금 = childPolicy.calculate(요금);
        int 청소년_요금 = youthPolicy.calculate(요금);
        int 성인_요금 = basicPolicy.calculate(요금);

        //then
        assertThat(어린이_요금).isEqualTo(450);
        assertThat(청소년_요금).isEqualTo(720);
        assertThat(성인_요금).isEqualTo(요금);
    }

    private void 연령별_요금정책_조회() {
        childPolicy = AgeFarePolicy.findByAge(10);
        youthPolicy = AgeFarePolicy.findByAge(16);
        basicPolicy = AgeFarePolicy.findByAge(50);
    }
}
