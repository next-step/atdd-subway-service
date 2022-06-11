package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Fare 클래스 테스트")
public class FareTest {

    @DisplayName("Fare를 생성한다.")
    @Test
    void successfulCreate() {
        Fare fare = new Fare(10);
        assertThat(fare.getValue()).isEqualTo(10);
    }

    @DisplayName("Fare은 null이거나 0미만이 될 수 없다.")
    @Test
    void failureCreate() {
        assertThatThrownBy(() -> {
            new Fare(-1);
        }).isInstanceOf(InvalidFareException.class)
        .hasMessageContaining("유효하지 않은 요금입니다.");
    }

    @DisplayName("Fare를 더한다.")
    @Test
    void plus() {
        Fare ten = new Fare(10);
        Fare one = new Fare(1);
        assertThat(ten.plus(one).getValue()).isEqualTo(11);
    }

    @DisplayName("Fare를 뺀다.")
    @Test
    void minus() {
        Fare ten = new Fare(10);
        Fare one = new Fare(1);
        assertThat(ten.minus(one).getValue()).isEqualTo(9);
    }
}
