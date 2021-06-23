package nextstep.subway.path.domain.fee;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class RequireFeeTest {

  @DisplayName("거리에 맞는 요금을 반환한다.")
  @CsvSource(value = {"0:0", "10:1250", "11:1350", "20:1450", "50:2050", "51:2150", "60:2250", "100:2750"}, delimiter = ':')
  @ParameterizedTest
  void requireFeeTest(double givenDistance, long expectFee) {
    assertThat(RequireFee.getRequireFee(givenDistance)).isEqualTo(expectFee);
  }

}
