package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidDistanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {

  @DisplayName("역 간 거리는 0 이하일 수 없다.")
  @ValueSource(ints = {-1, 0})
  @ParameterizedTest
  void MinimumDistanceTest(int invalidDistance) {
    assertThatThrownBy(() -> Distance.from(invalidDistance)).isInstanceOf(InvalidDistanceException.class);
  }

  @DisplayName("역 간 거리 끼리 더할 수 있다.")
  @Test
  void addTest() {
    //given
    Distance one = Distance.from(1);
    Distance two = Distance.from(2);

    //when & then
    assertThat(one.add(two)).isEqualTo(Distance.from(3));
  }
  @DisplayName("역 간 거리 끼리 뺄 수 있다.")
  @Test
  void subtractTest() {
    //given
    Distance one = Distance.from(1);
    Distance two = Distance.from(2);

    //when & then
    assertThat(two.subtract(one)).isEqualTo(Distance.from(1));
  }

  @DisplayName("기준이 되는 거리가 다른 거리보다 길이가 긴지 알 수 있다.")
  @Test
  void isFartherThanTest() {
    //given
    Distance one = Distance.from(1);
    Distance two = Distance.from(2);

    //when & then
    assertThat(two.isFartherThan(one)).isTrue();
  }


}
