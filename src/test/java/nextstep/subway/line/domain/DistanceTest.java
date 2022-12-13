package nextstep.subway.line.domain;

import nextstep.subway.enums.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class DistanceTest {


    @DisplayName("거리 계산시 기존 역과 역 사이의 거리보다 좁은 거리를 입력할 수 없다")
    @Test
    void 거리_유효성_검증(){
        // given
        Distance distance = new Distance(7);
        // when && then
        assertThatThrownBy(()-> distance.subtract(new Distance(8)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.EXCEEDED_DISTANCE.getMessage());
    }

}