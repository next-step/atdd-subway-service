package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("요금 할인 정책 테스트")
class FareDiscountPolicyTest {
  private static final int ADULT_AGE = 19;
  private static final int YOUTH_AGE = 18;
  private static final int CHILD_AGE = 12;

  @DisplayName("나이별로 적용되는 요금 할인 정책을 검증한다.")
  @ParameterizedTest
  @CsvFileSource(resources = "/csv/farePolicyTestData.csv", delimiter = ',', numLinesToSkip = 1)
  void 나이별_요금_할인_정책_검증(int distance, int actualFare, int actualYouthFare, int actualChildFare) {
    assertAll(
            () -> assertThat(FareDiscountPolicy.getDiscountFare(ADULT_AGE, FarePolicy.getTotalFare(distance))).isEqualTo(actualFare),
            () -> assertThat(FareDiscountPolicy.getDiscountFare(YOUTH_AGE, FarePolicy.getTotalFare(distance))).isEqualTo(actualYouthFare),
            () -> assertThat(FareDiscountPolicy.getDiscountFare(CHILD_AGE, FarePolicy.getTotalFare(distance))).isEqualTo(actualChildFare)
    );
  }
}