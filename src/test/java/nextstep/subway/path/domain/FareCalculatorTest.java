package nextstep.subway.path.domain;

import static nextstep.subway.line.domain.LineFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;

@DisplayName("요금 계산기")
class FareCalculatorTest {
	private FareCalculator fareCalculator;

	@BeforeEach
	public void setUp() {
		fareCalculator = new FareCalculator(
			new PerOverchargeFareSectionDistanceBasedFarePolicy(),
			new MostExpensiveLineOverchargeFarePolicy(),
			new PerAgeMemberDiscountFarePolicy());
	}

	@DisplayName("요금을 계산한다.")
	@ParameterizedTest
	@MethodSource("calculateMethodSource")
	void calculate(
		int distance,
		List<Line> lines,
		LoginMember loginMember,
		int expectedFare
	) {
		// given

		// when
		int actualFare = fareCalculator.calculate(distance, lines, loginMember);

		// then
		assertThat(actualFare).isEqualTo(expectedFare);
	}

	private static List<Arguments> calculateMethodSource() {
		return Arrays.asList(
			// 거리별 요금 정책
			Arguments.of(
				5,
				Collections.singletonList(삼호선()),
				LoginMember.notLoggedIn(),
				1250
			),
			Arguments.of(
				10,
				Collections.singletonList(삼호선()),
				LoginMember.notLoggedIn(),
				1250
			),
			Arguments.of(
				11,
				Collections.singletonList(삼호선()),
				LoginMember.notLoggedIn(),
				1350
			),
			Arguments.of(
				12,
				Collections.singletonList(삼호선()),
				LoginMember.notLoggedIn(),
				1350
			),
			Arguments.of(
				15,
				Collections.singletonList(삼호선()),
				LoginMember.notLoggedIn(),
				1350
			),
			Arguments.of(
				16,
				Collections.singletonList(삼호선()),
				LoginMember.notLoggedIn(),
				1450
			),
			Arguments.of(
				25,
				Collections.singletonList(삼호선()),
				LoginMember.notLoggedIn(),
				1550
			),
			Arguments.of(
				50,
				Collections.singletonList(삼호선()),
				LoginMember.notLoggedIn(),
				2050
			),
			Arguments.of(
				51,
				Collections.singletonList(삼호선()),
				LoginMember.notLoggedIn(),
				2150
			),
			Arguments.of(
				55,
				Collections.singletonList(삼호선()),
				LoginMember.notLoggedIn(),
				2150
			),
			Arguments.of(
				58,
				Collections.singletonList(삼호선()),
				LoginMember.notLoggedIn(),
				2150
			),
			Arguments.of(
				59,
				Collections.singletonList(삼호선()),
				LoginMember.notLoggedIn(),
				2250
			),
			// 거리별 요금 정책 + 노선별 추가 요금 정책
			Arguments.of(
				10,
				Arrays.asList(신분당선(), 이호선()),
				LoginMember.notLoggedIn(),
				1250 + 900
			),
			Arguments.of(
				10,
				Arrays.asList(이호선(), 삼호선()),
				LoginMember.notLoggedIn(),
				1250 + 500
			),
			Arguments.of(
				10,
				Collections.singletonList(삼호선()),
				LoginMember.notLoggedIn(),
				1250
			),
			// 거리별 요금 정책 + 노선별 추가 요금 정책 + 회원별 할인 정책
			Arguments.of(
				10,
				Collections.singletonList(삼호선()),
				LoginMember.loggedIn(1L, "dummy@gmail.com", 6),
				(int)((1250 - 350) * (1 - 0.5))
			),
			Arguments.of(
				10,
				Collections.singletonList(삼호선()),
				LoginMember.loggedIn(1L, "dummy@gmail.com", 12),
				(int)((1250 - 350) * (1 - 0.5))
			),
			Arguments.of(
				10,
				Collections.singletonList(삼호선()),
				LoginMember.loggedIn(1L, "dummy@gmail.com", 13),
				(int)((1250 - 350) * (1 - 0.2))
			),
			Arguments.of(
				10,
				Collections.singletonList(삼호선()),
				LoginMember.loggedIn(1L, "dummy@gmail.com", 18),
				(int)((1250 - 350) * (1 - 0.2))
			),
			Arguments.of(
				10,
				Collections.singletonList(삼호선()),
				LoginMember.loggedIn(1L, "dummy@gmail.com", 19),
				1250
			),
			Arguments.of(
				10,
				Collections.singletonList(삼호선()),
				LoginMember.loggedIn(1L, "dummy@gmail.com", 28),
				1250
			),
			Arguments.of(
				10,
				Collections.singletonList(삼호선()),
				LoginMember.notLoggedIn(),
				1250
			));
	}
}
