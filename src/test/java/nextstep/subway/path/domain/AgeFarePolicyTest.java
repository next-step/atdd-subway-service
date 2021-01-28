package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class AgeFarePolicyTest {
	@DisplayName("6이상 13미안 어린이는 운임에서 350원을 공제한 금액의 50%를 할인 받는다.")
	@ParameterizedTest
	@CsvSource({"6,950,300", "12,2000,825"})
	void calculateDiscountedFareForChild(int age, int fare, int discountedFare) {
		assertThat(AgeFarePolicy.find(age).calculateDiscountedFare(fare)).isEqualTo(discountedFare);
	}

	@DisplayName("13이상 19미안 청소년 운임에서 350원을 공제한 금액의 20%를 할인 받는다.")
	@ParameterizedTest
	@CsvSource({"13,950,480", "18,2000,1320"})
	void calculateDiscountedFareForTeenager(int age, int fare, int discountedFare) {
		assertThat(AgeFarePolicy.find(age).calculateDiscountedFare(fare)).isEqualTo(discountedFare);
	}

	@DisplayName("6세 미만 어린이, 19세 이상 성인은 할인을 받지 못한다.")
	@ParameterizedTest
	@CsvSource({"5,950,950", "19,2000,2000"})
	void calculateDiscountedFareForOthers(int age, int fare, int discountedFare) {
		assertThat(AgeFarePolicy.find(age).calculateDiscountedFare(fare)).isEqualTo(discountedFare);
	}
}
