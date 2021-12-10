package nextstep.subway.member.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("단위 테스트 - 나이 도메인")
class AgeTest {

	@DisplayName("현재 나이가 청소년인지 확인하는 메소드 테스트")
	@ParameterizedTest(name = "index {0} ==> age {1}, result {2}")
	@CsvSource(value = {
		"13:true", "11:false", "18:true", "19:false"
	}, delimiter = ':')
	void isTeenager(int age, boolean exResult) {
		// given
		Age givenAge = Age.from(age);

		// when
		boolean result = givenAge.isTeenager();

		// then
		assertThat(result).isEqualTo(exResult);
	}

	@DisplayName("현재 나이가 어린이인지 확인하는 메소드 테스트")
	@ParameterizedTest(name = "index {0} ==> age {1}, result {2}")
	@CsvSource(value = {
		"13:false", "11:true", "18:false", "19:false", "6:true", "12:true"
	}, delimiter = ':')
	void isChild(int age, boolean exResult) {
		// given
		Age givenAge = Age.from(age);

		// when
		boolean result = givenAge.isChild();

		// then
		assertThat(result).isEqualTo(exResult);
	}
}
