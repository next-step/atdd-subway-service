package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.exception.InvalidArgumentException;
import nextstep.subway.path.domain.AgeFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("운임 클래스 태스트")
public class FareTest {

    @DisplayName("운임 클래스 생성 시 정합성 검증 실패 ")
    @ParameterizedTest
    @ValueSource(ints = {-100, -3})
    void validateFail(int fare) {
        assertThatThrownBy(() -> Fare.valueOf(fare))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessageContaining("이상의 정수만 입력가능합니다.");
    }

    @DisplayName("연령별 금액")
    @Test
    void ageFare() {
        Fare fare = Fare.valueOf(1350);
        assertAll(() -> {
            assertThat(fare.getAgeFare(AgeFarePolicy.FREE)).isEqualTo(Fare.valueOf(0));
            assertThat(fare.getAgeFare(AgeFarePolicy.CHILDREN)).isEqualTo(Fare.valueOf(500));
            assertThat(fare.getAgeFare(AgeFarePolicy.TEENAGER)).isEqualTo(Fare.valueOf(800));
            assertThat(fare.getAgeFare(AgeFarePolicy.GENERAL)).isEqualTo(fare);
        });
    }
}
