package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.auth.domain.LoginMember;

class DiscountTest {

	@ParameterizedTest
	@CsvSource(value = {"0:BABY", "6:CHILD", "13:YOUTH", "19:ADULT"}, delimiter = ':')
	@DisplayName("할인 등급 확인 테스트")
	public void checkGrade(int age , String expected) {
		//given
		LoginMember loginMember = new LoginMember(null,null, age);

		//when
		String name = Arrays.stream(Discount.values())
			.filter(overFare -> overFare.checkGrade(loginMember))
			.findFirst().get()
			.name();

		//then
		assertThat(name).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource(value = {"0:0", "6:450", "13:720", "19:1250"}, delimiter = ':')
	@DisplayName("요금 할인 테스트")
	public void discount(int age , int expected) {
		//given
		LoginMember loginMember = new LoginMember(null,null, age);

		//when
		SubwayFare discount = Arrays.stream(Discount.values())
			.filter(overFare -> overFare.checkGrade(loginMember))
			.findFirst().get()
			.discount(SubwayFare.of(new BigDecimal(1250)));

		//then
		assertThat(discount.value()).isEqualTo(expected);
	}
}
