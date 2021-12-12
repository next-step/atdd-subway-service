package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nextstep.subway.auth.domain.LoginMember;

@DisplayName("회원별 요금 할인 정책")
class MemberDiscountFarePolicyTest {


	@DisplayName("회원 나이별로 요금을 할인한다.")
	@ParameterizedTest
	@MethodSource("discountPerAgeMethodSource")
	void discountPerAgeMethodSource(int fare, LoginMember loginMember, int expectedFare) {
		// given
		MemberDiscountFarePolicy policy = new PerAgeMemberDiscountFarePolicy();

		// when
		int actualFare = policy.discount(fare, loginMember);

		// then
		assertThat(actualFare).isEqualTo(expectedFare);
	}

	private static List<Arguments> discountPerAgeMethodSource() {
		return Arrays.asList(
			Arguments.of(
				1250,
				LoginMember.loggedIn(1L, "dummy@gmail.com", 6),
				(int)((1250 - 350) * (1 - 0.5))
			),
			Arguments.of(
				1250,
				LoginMember.loggedIn(1L, "dummy@gmail.com", 12),
				(int)((1250 - 350) * (1 - 0.5))
			),
			Arguments.of(
				1250,
				LoginMember.loggedIn(1L, "dummy@gmail.com", 13),
				(int)((1250 - 350) * (1 - 0.2))
			),
			Arguments.of(
				1250,
				LoginMember.loggedIn(1L, "dummy@gmail.com", 18),
				(int)((1250 - 350) * (1 - 0.2))
			),
			Arguments.of(
				1250,
				LoginMember.loggedIn(1L, "dummy@gmail.com", 19),
				1250
			),
			Arguments.of(
				1250,
				LoginMember.loggedIn(1L, "dummy@gmail.com", 28),
				1250
			),
			Arguments.of(
				1250,
				LoginMember.notLoggedIn(),
				1250
			));
	}
}
