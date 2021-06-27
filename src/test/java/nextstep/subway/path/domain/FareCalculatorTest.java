package nextstep.subway.path.domain;

import static nextstep.subway.auth.domain.LoginMember.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Fare;

class FareCalculatorTest {

	@DisplayName("비로그인 사용자는 경로 요금이 최종 요금이다.")
	@Test
	void anonymousMemberTest() {
		Fare expectedFare = Fare.wonOf(2000);
		Path path = mock(Path.class);
		when(path.calculatePathFare()).thenReturn(expectedFare);

		Fare fare = FareCalculator.calculateFare(path, ANONYMOUS);

		assertThat(fare).isEqualTo(expectedFare);
	}

	@DisplayName("로그인 사용자는 경로 요금에 할인이 적용될 수 있다.")
	@Test
	void loginMemberTest() {
		Path path = mock(Path.class);
		when(path.calculatePathFare()).thenReturn(Fare.wonOf(2000));
		LoginMember childMember = new LoginMember(1L, "test@test.com", 10);

		Fare fare = FareCalculator.calculateFare(path, childMember);

		assertThat(fare).isEqualTo(Fare.wonOf(825));
	}

}
