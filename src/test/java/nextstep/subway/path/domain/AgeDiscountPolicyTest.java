package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class AgeDiscountPolicyTest {
	private BigDecimal charge;

	@BeforeEach
	void setUp() {
		charge = BigDecimal.valueOf(1250);
	}

	@ParameterizedTest(name = "나이가 13세 이상 19세 미만인 경우는 운임에서 350원을 공제한 금액의 20%를 할인 받는다.")
	@ValueSource(ints = {13, 15, 18})
	void discountTestForTeenager(int age) {
		AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.valueByMatchedAge(new LoginMember(age));
		assertThat(ageDiscountPolicy.apply(charge)).isEqualTo(BigDecimal.valueOf(720.0));
	}

	@ParameterizedTest(name = "나이가 6세 이상 13세 미만인 경우는 운임에서 350원을 공제한 금액의 50%를 할인 받는다.")
	@ValueSource(ints = {6, 10, 12})
	void discountTestForChildren(int age) {
		AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.valueByMatchedAge(new LoginMember(age));
		assertThat(ageDiscountPolicy.apply(charge)).isEqualTo(BigDecimal.valueOf(450.0));
	}

	@ParameterizedTest(name = "그 외에는 할인 받지 않는다.")
	@ValueSource(ints = {3, 19, 22, 30})
	void noDiscountTest(int age) {
		AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.valueByMatchedAge(new LoginMember(age));
		assertThat(ageDiscountPolicy.apply(charge)).isEqualTo(BigDecimal.valueOf(1250.0));
	}
}
