package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("연령대별 할인 정책")
class AgeFarePolicyTest {

    @DisplayName("입력한 나이에 대해 적절한 할인정책을 가져올 수 있다.")
    @Test
    void 입력한_연령의_할인정책() {
        assertAll(() -> assertThat(AgeFarePolicy.findApplicableAgeFarePolicy(13)).hasValue(AgeFarePolicy.TEENAGER),
                () -> assertThat(AgeFarePolicy.findApplicableAgeFarePolicy(12)).hasValue(AgeFarePolicy.CHILDREN),
                () -> assertThat(AgeFarePolicy.findApplicableAgeFarePolicy(19)).isEmpty());
    }

    @DisplayName("전체 운임에 연령별 할인 정책을 적용할 수 있다.")
    @Test
    void 연령별_할인_정책_적용() {
        AgeFarePolicy 어린이_할인 = AgeFarePolicy.CHILDREN;

        final int 전체_운임 = 1000;
        assertThat(어린이_할인.applyPolicy(전체_운임)).isEqualTo(325);
    }
}
