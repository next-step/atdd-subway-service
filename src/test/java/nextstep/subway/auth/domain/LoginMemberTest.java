package nextstep.subway.auth.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.path.domain.SubwayFare;

class LoginMemberTest {

	@Test
	@DisplayName("생성 테스트")
	public void create() {
		//when
		LoginMember loginMember = new LoginMember(1L, "email@naver.com", 22);
		//then
		assertThat(loginMember).isNotNull();
	}

	@ParameterizedTest
	@CsvSource(value = {"6,6,13:true", "13,13,19:true", "1,6,13:false"}, delimiter = ':')
	@DisplayName("로그인 사용자가 속한 나이 확인 테스트")
	public void calculateChildDiscountFare(String age, boolean expected) {
		//given
		String[] split = age.split(",");
		LoginMember loginMember = new LoginMember(1L, "email@naver.com", Integer.parseInt(split[0]));
		//when
		boolean result = loginMember.checkAge(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
		//then
		assertThat(result).isEqualTo(expected);
	}

}
