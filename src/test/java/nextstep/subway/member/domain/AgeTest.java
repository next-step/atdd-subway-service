package nextstep.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AgeTest {

    @DisplayName("나이 값을 설정할 때 음수이면 예외가 발생해야 한다")
    @Test
    void createAgeByMinusAge() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Age(-1));
    }

    @DisplayName("나이를 설정할 때 음수가 아닌 나이를 생성하면 정상 생성되어야 한다")
    @Test
    void createAgeTest() {
        assertThatNoException().isThrownBy(() -> new Age(0));
        assertThatNoException().isThrownBy(() -> new Age(1));
        assertThatNoException().isThrownBy(() -> new Age(Integer.MAX_VALUE));
    }
}