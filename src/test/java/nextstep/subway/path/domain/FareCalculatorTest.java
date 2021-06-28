package nextstep.subway.path.domain;

import org.junit.jupiter.api.Test;

import nextstep.subway.auth.domain.LoginMember;

class FareCalculatorTest {

	@Test
	void testCalculateFare() {

		Path path = new Path(null, 16);
		LoginMember loginMember = new LoginMember(1L, "email@email.com", 20);

		FareCalculator calculator = new FareCalculator();
		calculator.calculateFare(path, loginMember);

	}
}
