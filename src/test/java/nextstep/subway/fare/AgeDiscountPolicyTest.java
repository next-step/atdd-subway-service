package nextstep.subway.fare;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.fare.domain.AgeDiscountPolicy;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.member.domain.Age;

@DisplayName("나이별 할인 정책 도메인 테스트")
public class AgeDiscountPolicyTest {

	@DisplayName("나이에 따른 할인 적용")
	@CsvSource({"19,1000,1000", "18,1000,800", "12,1000,500"})
	@ParameterizedTest
	void discountAgeTest(int age, int fare, int expected) {
		// when
		Fare result = AgeDiscountPolicy.discountByAge(Fare.of(fare), Age.of(age));

		// then
		assertThat(result).isEqualTo(Fare.of(expected));
	}
}
