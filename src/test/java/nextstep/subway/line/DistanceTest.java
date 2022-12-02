package nextstep.subway.line;

import nextstep.subway.line.domain.TempDistance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("구간 길이 관련 기능")
public class DistanceTest {

    @DisplayName("길이 객체 생성 후 동일한 객체인지 테스트")
    @Test
    void create_distance_equal_test() {
        assertThat(new TempDistance(10)).isEqualTo(new TempDistance(10));
    }

    @DisplayName("길이 생성 오류 테스트")
    @Test
    void distance_lower_value_exception_test() {
        assertThatThrownBy(() -> new TempDistance(0)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> new TempDistance(-1)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("길이 차감 테스트")
    @Test
    void distance_abstract_test() {
        assertThat((new TempDistance(10)).subtract(new TempDistance(5)))
                .isEqualTo(new TempDistance(5));
    }

    @DisplayName("길이 차감 오류 테스트")
    @Test
    void distance_abstract_exception_test() {
        assertThatThrownBy(() -> (new TempDistance(10)).subtract(new TempDistance(10)))
                .isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> (new TempDistance(10)).subtract(new TempDistance(15)))
                .isInstanceOf(RuntimeException.class);
    }
}
