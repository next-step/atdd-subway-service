package nextstep.subway.path.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RatePolicyByAgeTest {
    private int 어린이_나이 = 6;
    private int 청소년_나이 = 18;
    private int 어른_나이 = 30;

    @DisplayName("연령별 요금 할인 적용확인")
    @Test
    public void step1() {
        assertAll(
            () -> assertThat(new RatePolicyByAge(어린이_나이).calc(1250)).isEqualTo(450),
            () -> assertThat(new RatePolicyByAge(청소년_나이).calc(1250)).isEqualTo(720),
            () -> assertThat(new RatePolicyByAge(어른_나이).calc(1250)).isEqualTo(1250),

            () -> assertThat(new RatePolicyByAge(어린이_나이).calc(3650)).isEqualTo(1650),
            () -> assertThat(new RatePolicyByAge(청소년_나이).calc(3650)).isEqualTo(2640),
            () -> assertThat(new RatePolicyByAge(어른_나이).calc(3650)).isEqualTo(3650)
        );
    }
}