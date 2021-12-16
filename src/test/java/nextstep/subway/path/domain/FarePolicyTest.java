package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 정책 테스트")
class FarePolicyTest {
  @DisplayName("거리별로 적용되는 요금 정책을 검증한다.")
  @ParameterizedTest
  @CsvFileSource(resources = "/csv/farePolicyTestData.csv", delimiter = ',', numLinesToSkip = 1)
  void 거리별_요금_정책_검증(int distance, int actualFare) {
    assertThat(FarePolicy.getTotalFare(distance)).isEqualTo(actualFare);
  }
}