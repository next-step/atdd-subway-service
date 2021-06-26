package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.line.domain.Fare;

@DisplayName("나이별 할인 정책 테스트")
class AgeDiscountGradeTest {

	@DisplayName("5세 이하는 무료이다")
	@ParameterizedTest
	@ValueSource(ints = {1, 5})
	void toddlerDiscountFare(int toddlerAge) {
		AgeDiscountGrade discountGrade = AgeDiscountGrade.of(toddlerAge);

		Fare fare = discountGrade.discountFare(Fare.wonOf(1250));

		assertThat(fare).isEqualTo(Fare.wonOf(0));
	}

	@DisplayName("6세 이상 13세 미만 나이는 원래 운임에서 350원을 공제한 금액의 50%를 할인해준다.")
	@ParameterizedTest
	@ValueSource(ints = {6, 12})
	void childrenDiscountFare(int childrenAge) {
		AgeDiscountGrade discountGrade = AgeDiscountGrade.of(childrenAge);

		Fare fare = discountGrade.discountFare(Fare.wonOf(1250));

		assertThat(fare).isEqualTo(Fare.wonOf(450));
	}

	@DisplayName("13세 이상 19세 미만 나이는 원래 운임에서 350원을 공제한 금액의 20%를 할인해준다.")
	@ParameterizedTest
	@ValueSource(ints = {13, 18})
	void studentDiscountFare(int studentAge) {
		AgeDiscountGrade discountGrade = AgeDiscountGrade.of(studentAge);

		Fare fare = discountGrade.discountFare(Fare.wonOf(1250));

		assertThat(fare).isEqualTo(Fare.wonOf(720));
	}

	@DisplayName("19세 이상의 나이는 할인이 없다.")
	@ParameterizedTest
	@ValueSource(ints = {19, 99})
	void adultDiscountFare(int adultAge) {
		AgeDiscountGrade discountGrade = AgeDiscountGrade.of(adultAge);

		Fare fare = discountGrade.discountFare(Fare.wonOf(1250));

		assertThat(fare).isEqualTo(Fare.wonOf(1250));
	}
}