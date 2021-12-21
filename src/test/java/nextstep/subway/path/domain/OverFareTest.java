package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;

class OverFareTest {

	@ParameterizedTest
	@CsvSource(value = {"10:NONE_OVER_FARE", "50:FIRST_SECTION", "51:SECOND_SECTION"}, delimiter = ':')
	@DisplayName("추가 요금 구간 확인 테스트")
	public void checkGrade(int distance , String expected) {
		//when
		String name = Arrays.stream(OverFare.values())
			.filter(overFare -> overFare.checkGrade(Distance.of(distance)))
			.findFirst().get()
			.name();

		//then
		assertThat(name).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource(value = {"10:0", "50:800", "59:1000"}, delimiter = ':')
	@DisplayName("추가요금 계산 테스트")
	public void calculateOverFare(int distance , int expected) {
		//when
		SubwayFare calculate = Arrays.stream(OverFare.values())
			.filter(overFare -> overFare.checkGrade(Distance.of(distance)))
			.findFirst().get()
			.calculate(Distance.of(distance));

		//then
		assertThat(calculate.value()).isEqualTo(expected);
	}

}
