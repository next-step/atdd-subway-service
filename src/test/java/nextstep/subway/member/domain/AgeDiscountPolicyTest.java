package nextstep.subway.member.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.domain.Age;
import nextstep.subway.common.domain.Email;
import nextstep.subway.common.domain.Fare;

@DisplayName("나이 할인 정책 테스트")
class AgeDiscountPolicyTest {

	@DisplayName("유아 요금 테스트")
	@ParameterizedTest(name = "[{index}] 나이: {0} -> 정상 요금: {1} | 할인 요금: {2}")
	@CsvSource(value = {"1:1250:0", "3:5350:0", "5:2500:0"}, delimiter = ':')
	void discountFareForToddler(int age, int totalFare, int expected) {
		// given
		LoginMember loginMember = LoginMember.of(1L, Email.from("toddler@email.com"), Age.from(age));

		// when
		Fare fare = AgeDiscountPolicy.discountFare(loginMember, Fare.from(totalFare));

		// then
		assertThat(fare).isEqualTo(Fare.from(expected));
	}

	@DisplayName("어린이 요금 테스트")
	@ParameterizedTest(name = "[{index}] 나이: {0} -> 정상 요금: {1} | 할인 요금: {2}")
	@CsvSource(value = {"6:1250:450", "8:5350:2500", "12:2500:1075"}, delimiter = ':')
	void discountFareForKids(int age, int totalFare, int expected) {
		// given
		LoginMember loginMember = LoginMember.of(1L, Email.from("kids@email.com"), Age.from(age));

		// when
		Fare fare = AgeDiscountPolicy.discountFare(loginMember, Fare.from(totalFare));

		// then
		assertThat(fare).isEqualTo(Fare.from(expected));
	}

	@DisplayName("청소년 요금 테스트")
	@ParameterizedTest(name = "[{index}] 나이: {0} -> 정상 요금: {1} | 할인 요금: {2}")
	@CsvSource(value = {"13:1250:720", "15:5350:4000", "18:2500:1720"}, delimiter = ':')
	void discountFareForTeenager(int age, int totalFare, int expected) {
		// given
		LoginMember loginMember = LoginMember.of(1L, Email.from("teenager@email.com"), Age.from(age));

		// when
		Fare fare = AgeDiscountPolicy.discountFare(loginMember, Fare.from(totalFare));

		// then
		assertThat(fare).isEqualTo(Fare.from(expected));
	}

	@DisplayName("성인 요금 테스트")
	@ParameterizedTest(name = "[{index}] 나이: {0} -> 정상 요금: {1} | 할인 요금: {2}")
	@CsvSource(value = {"19:1250:1250", "25:5350:5350", "40:2500:2500"}, delimiter = ':')
	void discountFareForAdults(int age, int totalFare, int expected) {
		// given
		LoginMember loginMember = LoginMember.of(1L, Email.from("adult@test.com"), Age.from(age));

		// when
		Fare fare = AgeDiscountPolicy.discountFare(loginMember, Fare.from(totalFare));

		// then
		assertThat(fare).isEqualTo(Fare.from(expected));
	}

	@DisplayName("비회원 요금 테스트")
	@ParameterizedTest(name = "[{index}] 정상 요금: {1} | 할인 요금: {2}")
	@CsvSource(value = {"1250:1250", "5350:5350", "2500:2500"}, delimiter = ':')
	void discountFareForGuests(int totalFare, int expected) {
		// given
		LoginMember guest = LoginMember.guest();

		// when
		Fare fare = AgeDiscountPolicy.discountFare(guest, Fare.from(totalFare));

		// then
		assertThat(fare).isEqualTo(Fare.from(expected));
	}
}