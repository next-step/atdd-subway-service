package nextstep.subway.path.domain.fee.discount;

import nextstep.subway.path.domain.fee.BaseCalculatedFee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeDiscountTest {

  @DisplayName("연령 대별로 알맞은 할인 전략을 반환")
  @CsvSource(value = {"4:TODDLER", "9:CHILDREN", "16:TEENAGER", "20:ADULT"}, delimiter = ':')
  @ParameterizedTest
  void selectStrategyTest(Integer age, AgeDiscount expectation) {
    assertThat(AgeDiscount.from(age)).isEqualTo(expectation);
  }

  @DisplayName("연령 대별로 알맞게 할인된 기본 금액을 반환")
  @CsvSource(value = {"4:0", "9:0.5", "16:0.8", "20:1"}, delimiter = ':')
  @ParameterizedTest
  void selectStrategyTest(Integer age, double expectationAfterDiscountRatio) {
    //given
    long givenFee = 1_250;
    long expectFee = (long) (deductBaseFee(age, givenFee) * expectationAfterDiscountRatio);

    //when & then
    assertThat(AgeDiscount.from(age).discount(givenFee)).isEqualTo(expectFee);
  }

  //성인이 아닐경우 기본 금액에서 350원 만큼 공제한다.
  private Long deductBaseFee(Integer age, long fee) {
    if (age < 19) {
      return fee - 350;
    }
    return fee;
  }

}
