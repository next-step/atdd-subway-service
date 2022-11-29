package nextstep.subway.line.unit;

import nextstep.subway.exception.InvalidRequestException;
import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("지하철 거리 기능")
public class DistanceTest {

    private final Distance distance = new Distance(10);

    @Test
    @DisplayName("거리 유효성 검사")
   void checkValidationSize() {
        assertThrows(InvalidRequestException.class, () -> distance.checkValidationSize(17));
    }

    @Test
   @DisplayName("거리 계산")
    void minusChangeDistance() {
        distance.minusChangeDistance(5);
        assertThat(distance.value()).isEqualTo(5);
    }

}
